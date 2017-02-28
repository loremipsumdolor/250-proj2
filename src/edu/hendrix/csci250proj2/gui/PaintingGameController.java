package edu.hendrix.csci250proj2.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import edu.hendrix.csci250proj2.DrawA;
import edu.hendrix.csci250proj2.User;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ProgressBar;
import javafx.event.ActionEvent;

public class PaintingGameController {
	@FXML 
	TextArea userfield;
	@FXML
	Button donepainting;
	@FXML
	Canvas picture;
	@FXML
	Label drawingPrompt;
	@FXML
	ProgressBar inkremaining;
	
	private User user;
	private int rating;
	private int otherRating;
	
	@FXML
	private void initialize() {
		TextInputDialog signInDialog = new TextInputDialog("walter");
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
