package edu.hendrix.csci250proj2.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import edu.hendrix.csci250proj2.DrawA;
import edu.hendrix.csci250proj2.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
	private double inkRemainingDubs = 1.0;
	
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
	}
	
	public void startDrag(MouseEvent event) {
		if (inkRemainingDubs != 0.005) {
			sx = event.getX();
			sy = event.getY();
		}
	}
	
	public void draw(MouseEvent event) {
		if (inkRemainingDubs >= 0.005) {
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
