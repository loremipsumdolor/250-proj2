package edu.hendrix.csci250proj2.gui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import edu.hendrix.csci250proj2.DrawA;
import edu.hendrix.csci250proj2.User;
import edu.hendrix.csci250proj2.network.socketHelper;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javafx.scene.input.MouseEvent;
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

public class PaintingGameController {
	@FXML
	ColorPicker colorChooser;
	@FXML 
	TextArea userfield;
	@FXML
	Button donepainting;
	@FXML
	Pane drawingArea;
	@FXML
	Label drawingPrompt;
	@FXML
	ProgressBar inkRemaining;
	@FXML
	HBox drawingStuff;
	@FXML
	VBox colorStuff;
	
	private User user;
	private int rating;
	private int otherRating;
	private double sx;
	private double sy;
	private Color currentColor = Color.BLACK;
	private double inkRemainingDubs;
	public socketHelper player2;
	
	@FXML
	private void initialize() {
		colorChooser.setValue(Color.BLACK);
		colorChooser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentColor = colorChooser.getValue();
			}
		});
		drawingArea.setOnMouseDragged(event -> draw(event));
		drawingArea.setOnMousePressed(event -> startDrag(event));
		drawingArea.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		drawingArea.setLayoutX(colorStuff.getWidth());
		drawingArea.setLayoutY(drawingStuff.getHeight());
		//WELCOME SCREEN
		TextInputDialog signInDialog = new TextInputDialog();
		signInDialog.setTitle("Painting Game");
		signInDialog.setHeaderText("Welcome to Painting Game");
		signInDialog.setContentText("Please enter your name:");
		Optional<String> result = signInDialog.showAndWait();
		
		
		if (result.isPresent()){
		    user = new User(result.get());
		}
		try {
			ArrayList<String> potentialDrawings = DrawA.readFile();
			drawingPrompt.setText(potentialDrawings.get(ThreadLocalRandom.current().nextInt(potentialDrawings.size())));
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
			//startHost
		} else if (Bresult.get() == buttonTypeTwo) {
		    startUser();
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
		
		
		try {
			player2.writeUserName(user.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			outputMessage(player2.readUserName(), AlertType.CONFIRMATION);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void startUser(){
		//CONNECTION SCREEN
		TextInputDialog tryConnectionDialog = new TextInputDialog();
		tryConnectionDialog.setTitle("Painting Game");
		tryConnectionDialog.setHeaderText("Painting Game Setup");
		tryConnectionDialog.setContentText("Please enter IP of the other player:");
		boolean connection = false;
				
				while(!connection){//Connection Loop if a connection isn't made the first try		
					
					Optional<String> IPresult = tryConnectionDialog.showAndWait();
					try{
						player2 = new socketHelper(IPresult.get(),3002);
						connection = true;
						outputMessage("Successfully Connected!", AlertType.INFORMATION);
					}catch(IOException | NoSuchElementException e){//Catch an IO Error
						if(IPresult.isPresent() && e.getMessage() == IPresult.get()){
							outputMessage("Invalid Address!", AlertType.ERROR);
						}else{
							outputMessage(e.toString(), AlertType.ERROR);
						}
					}
				}
	}
	
	public void startHost(){
		//CONNECTION SCREEN
		outputMessage("Waiting on User", AlertType.ERROR);
		boolean connection = false;
				
				while(!connection){//Connection Loop if a connection isn't made the first try		
					try{
						player2 = new socketHelper(3002);
						connection = true;
						outputMessage("Successfully Connected!", AlertType.INFORMATION);
					}catch(IOException e){//Catch an IO Error
							outputMessage(e.toString(), AlertType.ERROR);
					}
				}
	}
	
	public void startDrag(MouseEvent event) {
		sx = event.getX();
		sy = event.getY();
	}
	
	public void draw(MouseEvent event) {
		double fx = event.getX();
		double fy = event.getY();
		Line line = new Line(sx, sy, fx, fy);
		line.setStroke(currentColor);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		drawingArea.getChildren().add(line);
		sx = fx;
		sy = fy;
		inkRemainingDubs -= .001;
		inkRemaining.setProgress(inkRemainingDubs);
	}
	
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
		matchEndAlert.setContentText("You earned " + Integer.toString(otherRating) + " " + starText);
	}
}
