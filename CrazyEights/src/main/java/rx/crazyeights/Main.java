package rx.crazyeights;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rx.crazyeights.controller.GameController;
import rx.crazyeights.model.Game;
import rx.crazyeights.view.GameView;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Crazy Eights");
		
		Game game = new Game();
		GameView gameView = new GameView(game);
		new GameController(game, gameView);

		Scene scene = new Scene(gameView);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
