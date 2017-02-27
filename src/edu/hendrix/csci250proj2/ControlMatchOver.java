package edu.hendrix.csci250proj2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ControlMatchOver {

	
	@FXML 
	Button backtolobby;
	
	@FXML
	void initialize() {
		
	}
	
	@FXML
	void open() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("TitleScreen.fxml"));
			Pane root = (Pane) loader.load();

			ControlTitleScreen second = (ControlTitleScreen)loader.getController();
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			secondStage.setScene(scene);
			secondStage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		backtolobby.getScene().getWindow().hide();
	}
}
