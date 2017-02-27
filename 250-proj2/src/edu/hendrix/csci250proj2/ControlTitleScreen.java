package edu.hendrix.csci250proj2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import edu.hendrix.csci250proj2.ControlGameplay;
import edu.hendrix.csci250proj2.Main;

public class ControlTitleScreen {
	
	@FXML
	Button next;
	
	@FXML
	TextField screenname;
	
	
	@FXML
	void initialize() {
		
	}
	
	@FXML
	void open() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("PaintingGamePlayButtons.fxml"));
			BorderPane root = (BorderPane) loader.load();

			ControlGameplay second = (ControlGameplay)loader.getController();
			second.setWord(screenname.getText());
			
			Stage secondStage = new Stage();
			Scene scene = new Scene(root);
			secondStage.setScene(scene);
			secondStage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		next.getScene().getWindow().hide();
	}
	
	

}
