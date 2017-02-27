package edu.hendrix.csci250proj2;

import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;

public class ControlGameplay {
	
	@FXML
	private User user = new User();

	@FXML 
	TextArea userfield;
	
	@FXML
	Button donepainting;
	
	@FXML
	Canvas picture;
	
	@FXML
	TextField pleasedraw;
	
	@FXML
	ProgressBar inkremaining;
	
	@FXML
	void initialize() {
		try {
		pleasedraw.setText(DrawA.readFile());
		}catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void setWord(String word) {
		userfield.appendText(word + "\n");
		user.setName(word);
		
	}
	
	public void resetWord() {
		Text username =  new Text(user.getName());
		username.setFill(user.getColor());
		//How do I make this colored text appear in the textfield??
		
		
	}
	
	public void setDone() {
		user.setDone();
	}
	
	
	//Loads the controller for the rating screen
	@FXML
	void open() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("PaintingGameRate.fxml"));
			BorderPane root = (BorderPane) loader.load();

			ControlRating second = (ControlRating)loader.getController();
			
			//An attempt to make an image appear in the next window
			//SnapshotParameters parameters = new SnapshotParameters();
			// WritableImage wi = new WritableImage(600, 450);
			// WritableImage snapshot = picture.snapshot(new SnapshotParameters(), wi);
			// File output = new File("snapshot.png");
			// ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			secondStage.setScene(scene);
			secondStage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		userfield.getScene().getWindow().hide();
	}
}
