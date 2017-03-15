package edu.hendrix.csci250proj2.gui;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import edu.hendrix.csci250proj2.DrawSelect;
import edu.hendrix.csci250proj2.User;
import edu.hendrix.csci250proj2.network.socketHelper;
import edu.hendrix.csci250proj2.network.socketState;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.control.ProgressBar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class PaintingGameController {
	@FXML
	ColorPicker colorChooser;
	@FXML 
	TextArea userfield;
	@FXML
	Button donePainting;
	Button clearButton;
	@FXML
	ToggleButton eraseButton;
	@FXML
	Pane drawingArea;
	@FXML
	TextField drawingPrompt;
	@FXML
	ProgressBar inkRemaining;
	@FXML
	HBox drawingStuff;
	@FXML
	VBox colorStuff;
	@FXML
	Pane board;
	
	private User user;
	private int rating;
	private int otherRating;
	private double sx;
	private double sy;
	private Color currentColor = Color.BLACK;
	private double inkRemainingDubs = 1.0;
    private boolean ready = false;
    
    //Prompts
    ArrayList<String> potentialDrawings;
    String currentPrompt;
    
    //Networking
    private boolean isHost = false;
	private socketHelper player2;
	private boolean connection;
    private Thread socketReaderThread;
    private Thread setupThread;
	
	
	private Node cleanDrawingArea;

	@FXML
	private void initialize() {
		colorChooser.setValue(Color.BLACK);
		colorChooser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentColor = colorChooser.getValue();
				eraseButton.setSelected(false);
			}
		});
		drawingArea.setOnMouseDragged(event -> draw(event));
		drawingArea.setOnMousePressed(event -> startDrag(event));
		drawingArea.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		drawingArea.setLayoutX(colorStuff.getHeight());
		drawingArea.setLayoutY(drawingStuff.getWidth());
		
		cleanDrawingArea = drawingArea.getChildren().get(0);
		TextInputDialog signInDialog = new TextInputDialog();
		signInDialog.setTitle("Painting Game");
		signInDialog.setHeaderText("Welcome to Painting Game");
		signInDialog.setContentText("Please enter your name:");
		Optional<String> result = signInDialog.showAndWait();
		if (result.isPresent()){
		    user = new User(result.get());
		}else {
			user = new User("User1"); 
		}
		try {
			currentPrompt = DrawSelect.initialize();
		} catch (Exception exc) {
			outputMessage(exc.getMessage(), AlertType.ERROR);
		}
	}
	
    public void startUser(){
    	//CONNECTION SCREEN
		TextInputDialog tryConnectionDialog = new TextInputDialog();
		tryConnectionDialog.setTitle("Painting Game");
		tryConnectionDialog.setHeaderText("Painting Game Setup");
		tryConnectionDialog.setContentText("Please enter IP of the other player:");
				
		while(!ready){//Connection Loop if a connection isn't made the first try		
					
    		Optional<String> IPresult = tryConnectionDialog.showAndWait();
    		try{

    				notifyReady();
    				
    		}catch(NoSuchElementException e){
    				if(IPresult.isPresent() && e.getMessage() == IPresult.get()){
    						outputMessage("Invalid Address!", AlertType.ERROR);
    				}else{
    						outputMessage(e.toString(), AlertType.ERROR);
    				}
    		}
		}
		
    	try {
            waitForReady();
            /*
             * Background thread to continuously read from the input stream.
             */
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
            outputMessage("Userin it", AlertType.INFORMATION);
        } catch (Exception e) {
        	outputMessage(e.getMessage(), AlertType.INFORMATION);
        }
	}
	
	public void startHost(){
		//CONNECTION SCREEN
		isHost = true;
		drawingPrompt.setText(currentPrompt);
		try {
            /*
			 * Notify SocketReaderThread that it can now start.
			 */
			notifyReady();
             * Background thread to set up and open the input and
             * output data streams.
             */
            setupThread = new SetupServerThread();
            setupThread.start();
            
            waitForReady();
            /*
             * Background thread to continuously read from the input stream.
             */
            socketReaderThread = new SocketReaderThread();
            socketReaderThread.start();
            outputMessage("Hostin it", AlertType.INFORMATION);
        } catch (Exception e) {
        	outputMessage(e.getMessage(), AlertType.INFORMATION);
        }  
		
		
		
	}
    
	public void startDrag(MouseEvent event) {
		if (inkRemainingDubs >= 0.0025) {
			sx = event.getX();
			sy = event.getY();
		}
	}
	
	public void draw(MouseEvent event) {
		if (inkRemainingDubs >= 0.005) {
		//Edited so it doesn't draw everywhere
		if (inkRemainingDubs >= 0.0025) {
			double fx = event.getX();
			double fy = event.getY();
			Line line = new Line(sx, sy, fx, fy);
			line.setStroke(currentColor);
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			drawingArea.getChildren().add(line);
			sx = fx;
			sy = fy;
			inkRemainingDubs -= .005;
			inkRemaining.setProgress(inkRemainingDubs);
			if (fx > 62 && fx < 590 && fy > 65 && fy < 426) {
				if (!eraseButton.isSelected()) {
					Line line1 = new Line(sx, sy, fx, fy);
					line1.setStroke(currentColor);
					line1.setStrokeLineCap(StrokeLineCap.ROUND);
					drawingArea.getChildren().add(line1);
					sx = fx;
					sy = fy;
					inkRemainingDubs -= .001;
					inkRemaining.setProgress(inkRemainingDubs);
				} else if (eraseButton.isSelected() && fx > 72 && fx < 580 && fy > 75 && fy < 416) {
					Line line1 = new Line(sx, sy, fx, fy);
					line1.setStroke(currentColor);
					line1.setStrokeLineCap(StrokeLineCap.ROUND);
					line1.setStrokeWidth(20.0);
					drawingArea.getChildren().add(line1);
					sx = fx;
					sy = fy;
					}
				}
			}
		}
	}
	@FXML
	public void clearDrawing() {
		drawingArea.getChildren().clear();
		drawingArea.getChildren().add(cleanDrawingArea);
		inkRemainingDubs = 1.0;
		inkRemaining.setProgress(inkRemainingDubs);
	}
	
	@FXML
	private void eraseDrawing() {
		if (eraseButton.isSelected()) {
			currentColor = Color.WHITE;
			
		} else {
			currentColor = colorChooser.getValue();
		}
		
	}

	@FXML
	private void setDone() {
		user.setDone();
		rateDrawing();
		endOfMatch();
	}
	
	private void outputMessage(String message, AlertType alertType) {
		Alert alert = new Alert(alertType, message);
		alert.showAndWait();
	}
	
	public void rateDrawing() {
		ArrayList<String> ratings = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5"));
		ChoiceDialog<String> ratingDialog = new ChoiceDialog<>("0", ratings);
		ratingDialog.setTitle("Painting Game");
		ratingDialog.setHeaderText("Rate Your Opponent!");
		ratingDialog.setContentText("Select a rating:");
		Optional<String> result = ratingDialog.showAndWait();
		if (result.isPresent()){
		    rating = Integer.parseInt(result.get());
		}
	}
	
	private void endOfMatch() {
		String starText = null;
		Alert matchEndAlert = new Alert(AlertType.INFORMATION);
		matchEndAlert.setTitle("Painting Game");
		matchEndAlert.setHeaderText("Congratulations!");
		if(otherRating == 1){
			starText = "star";
		} else {
			starText = "stars";
		}
		matchEndAlert.setContentText("You earned " + Integer.toString(otherRating) + " " + starText + ".");
		matchEndAlert.showAndWait();
	}
	

    private synchronized void waitForReady() {
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /*
     * Synchronized method responsible for notifying waitForReady()
     * method that it's OK to stop waiting.
     */
    private synchronized void notifyReady() {
        ready = true;
        //outputMessage("Successfully Connected!", AlertType.INFORMATION);
        notifyAll();
    }
    
    /**
     * Called whenever the open/closed status of the Socket
     * changes.  In JavaFX, this method must be run on the main thread and is
     * accomplished by the Platform.runLater() call.  Failure to do so
     * *will* result in strange errors and exceptions.
     * @param isClosed true if the socket is closed
     */
    /*
    public void onClosedStatus(final boolean isClosed) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //fxListener.onClosedStatus(isClosed);
            	if(!isClosed){
            		connection = true;
            	}else{
            		connection = false;
            	}
            }
        });
    }*/
    
    public void promptReady() {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	drawingPrompt.setText(currentPrompt);
            }
        });
    }
    
    public void madeIT(String It) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	outputMessage(It, AlertType.INFORMATION);
            }
        });
    }
    
    class SetupServerThread extends Thread {

        @Override
        public void run() {
            try {
            	player2 = new socketHelper(3002);
            	player2.writeString(user.getName());
                /*
                 * Notify SocketReaderThread that it can now start.
                 */
                notifyReady();
            } catch (IOException e) {
                    //outputMessage("Waiting on User", AlertType.ERROR);
            	System.out.println(e.getMessage());
                /*
                 * This will notify the SocketReaderThread that it should exit.
                 */
                notifyReady();
            }
        }
    }

    class SocketReaderThread extends Thread {

        @Override
        public void run() {
            /*
             * Wait until the socket is set up before beginning to read.
             */
        	/////FOUR SOCKET STATES
        	while(true)
        	{
        		//USERNAME
        		if(player2.getState() == socketState.USERNAME)
        		{
        			try {
							user2 = new User(player2.readNextString());
							player2.setState(socketState.DRAWING);
							if(isHost){
								player2.writeString(currentPrompt);
							}
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
        		//PROMPT EXCHANGE
        		}else if(player2.getState() == socketState.DRAWING){
        			try {
						if(!isHost){
							currentPrompt = player2.readNextString();
							promptReady();
							player2.setState(socketState.COLOR);
						}else{
							player2.setState(socketState.COLOR);
							
						}
						
        			} catch (IOException e) {
        				System.out.println(e.getMessage());
        			}
        		//COLOR EXCHANGE
        		}else if(player2.getState() == socketState.COLOR){
        			
        		//SCORE EXCHANGE
        		}else if(player2.getState() == socketState.SCORE){
        			
        		}	
            }
        }
    }

	
>>>>>>> parent of 6aafdc5... Presentation Time
}
