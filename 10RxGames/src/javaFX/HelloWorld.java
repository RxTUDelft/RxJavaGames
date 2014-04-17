package javaFX;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {

	@Override
	public void start(Stage primaryStage) {
		//Button and it's action
		Button btn = new Button();
		btn.setText("Say 'Hello World'");
		btn.setOnAction(event -> System.out.println("Hello World!"));

		//Add button to pane
		StackPane root = new StackPane();
		root.getChildren().add(btn);

		//Make the screen panel, based on the StackPane and with certain dimensions
		Scene scene = new Scene(root, 300, 250);

		//Sets up the frame
		primaryStage.setTitle("Hello World!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}
