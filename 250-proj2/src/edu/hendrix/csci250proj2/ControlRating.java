package edu.hendrix.csci250proj2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControlRating {
	
	@FXML
	ImageView finalpic;
	
	@FXML
	Button ok;
	
	@FXML
	void initialize() {
		//Image image = new Image(getClass().getResourceAsStream("snapshot.png"));
		//finalpic.setImage(image);
		
	}
	
	@FXML
	void open() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("MatchOver.fxml"));
			Pane root = (Pane) loader.load();

			ControlMatchOver second = (ControlMatchOver)loader.getController();
			//second.setWord(screenname.getText());
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			secondStage.setScene(scene);
			secondStage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		finalpic.getScene().getWindow().hide();
	}
	
	
}
