package edu.hendrix.csci250proj2.gui;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;


import edu.hendrix.csci250proj2.DrawSelect;
import edu.hendrix.csci250proj2.User;
import edu.hendrix.csci250proj2.network.SocketReaderThread;
import edu.hendrix.csci250proj2.network.color;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
	
	//Users
	private User user;
	private int rating;
	
	//Drawing
	private double sx;
	private double sy;
	private Color currentColor = Color.BLACK;
	private double inkRemainingDubs = 1.0;
    private boolean ready = false;
    private int finalScore = 4;
    
    
    //Prompts
    ArrayList<String> potentialDrawings;
    String currentPrompt;
    
    //Networking
    private Thread socketReaderThread;

	
	
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
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Host or User");
		alert.setHeaderText("Choose to be the Host or the User");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeOne = new ButtonType("Host");
		ButtonType buttonTypeTwo = new ButtonType("User");
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> Bresult = alert.showAndWait();
		if (Bresult.get() == buttonTypeOne){
			startHost();
		} else if (Bresult.get() == buttonTypeTwo) {
		    startUser();
		} else {
		    // ... user chose CANCEL or closed the dialog
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
    				socketReaderThread = new SocketReaderThread(IPresult.get(),user.getName(),currentPrompt, this);
    				socketReaderThread.start();
    				notifyReady();
    				
    		}catch(IOException | NoSuchElementException e){//Catch an IO Error
    				if(IPresult.isPresent() && e.getMessage() == IPresult.get()){
    						outputMessage("Invalid Address!", AlertType.ERROR);
    				}else{
    						outputMessage(e.toString(), AlertType.ERROR);
    				}
    		}
		}
		
    	try {
            /*
             * Background thread to continuously read from the input stream.
             */
            
           
        } catch (Exception e) {
        	outputMessage("Socket Reader Error" + e.getMessage(), AlertType.INFORMATION);
        }
	}
	
	public void startHost(){
		//CONNECTION SCREEN
		drawingPrompt.setText(currentPrompt);
		try {
            /*
             * Background thread to set up and open the input and
             * output data streams.
             */
			 socketReaderThread = new SocketReaderThread(user.getName(),currentPrompt,this);
			 socketReaderThread.start();
             /*
              * Notify SocketReaderThread that it can now start.
              */
              notifyReady();
        } catch (Exception e) {
        	e.printStackTrace();
        	outputMessage("Socket Reader Error: ", AlertType.INFORMATION);
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
			((SocketReaderThread) socketReaderThread).addColToSendStack(new color(currentColor.getRed(),currentColor.getGreen(),currentColor.getBlue(),sx,sy,fx,fy));
			//System.out.println(sendStack.isEmpty());
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
					((SocketReaderThread) socketReaderThread).addColToSendStack(new color(currentColor.getRed(),currentColor.getGreen(),currentColor.getBlue(),sx,sy,fx,fy));
					//System.out.println(sendStack.isEmpty());
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
	
	public void drawer(double r, double g, double b,double sx, double sy,double fx, double fy) {
		//Edited so it doesn't draw everywhere
			Line line = new Line(sx, sy, fx, fy);
			line.setStroke(new Color(r,g,b,1));
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			drawingArea.getChildren().add(line);
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
		System.out.println("UserDone");
		ready = false;
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
		    ((SocketReaderThread) socketReaderThread).addColToSendStack(new color(0,0,0,0,0,0,0));
		    ((SocketReaderThread) socketReaderThread).sendFinalScore(rating);
		}else{
			((SocketReaderThread) socketReaderThread).addColToSendStack(new color(0,0,0,0,0,0,0));
			((SocketReaderThread) socketReaderThread).sendFinalScore(0);
		}
	}
	
	public void endOfMatch() {
		if(!ready){
			Alert userStillRating = new Alert(AlertType.INFORMATION);
			userStillRating.setTitle("Not Done Drawing Yet!!");
			userStillRating.setContentText("The other user hasn't rated you yet");
		}
		
		String starText = null;
		Alert matchEndAlert = new Alert(AlertType.INFORMATION);
		matchEndAlert.setTitle("Painting Game");
		matchEndAlert.setHeaderText("Congratulations!");
		if(finalScore == 1){
			starText = "star";
		} else {
			starText = "stars";
		}
		matchEndAlert.setContentText("You earned " + Integer.toString(finalScore) + " " + starText + ".");
		matchEndAlert.showAndWait();
	}
	
	public void setFinalRating(int score){
		this.finalScore = score;
		notifyReady();
	}
	
	/*
     * Synchronized method set up to wait until the SetupThread is
     * sufficiently initialized.  When notifyReady() is called, waiting
     * will cease.
     */
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
        notifyAll();
    }
    
    public void promptReady(String promptText) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	ready = false;
            	currentPrompt = promptText;
            	drawingPrompt.setText(promptText);
            }
        });
    } 
    
	public void setUserArea(String username){
		javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	userfield.appendText(username);
            }
        });
		
	}
    
    public void drawFX(color col) {
        javafx.application.Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	drawer(col.getR(),col.getG(),col.getB(),col.getSX(),col.getSY(),col.getFX(),col.getFY());
            }
        });
    }
}
    