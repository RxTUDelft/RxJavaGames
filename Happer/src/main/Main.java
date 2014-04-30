package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.MainFrame;

public class Main extends Application {	
	public static Scene scene;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Happer");
		
		StackPane root = new StackPane();
		root.getChildren().add(new MainFrame());
		
		scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
}
