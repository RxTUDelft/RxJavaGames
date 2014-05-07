package rx.happer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.happer.view.MainFrame;

public class Main extends Application {	
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Happer");
		
		StackPane root = new StackPane();
		root.getChildren().add(new MainFrame());
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch();
	}
}
