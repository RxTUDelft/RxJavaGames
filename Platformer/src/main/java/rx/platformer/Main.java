package rx.platformer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import rx.platformer.controller.Observables;
import rx.platformer.model.Level;
import rx.platformer.model.LevelGenerator;
import rx.platformer.view.LevelView;
import rx.platformer.view.LostView;
import rx.platformer.view.WonView;

public class Main extends Application {
	Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Platformer");
		
		startGame(primaryStage);
		
		primaryStage.show();
	}
	
	public void startGame(Stage primaryStage) {
		LevelView levelView = new LevelView();
		scene = new Scene(levelView, 1000, 630);
		
		char[][] levelInput = {
				{' ',' ','C',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','K',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ','K',' ',' ',' ',' ',' ',' ','C',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','C','K',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','C',' ',' ',' ','b',' ',' ',' ',' ','F','b',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','C',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','b',' ',' ',' ',' ',' ',' ','F','b',' ',' ',' ',' ',' ',' ',' ','C',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' ',' ','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','b',' ',' ',' ',' ','M',' ','b',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','C',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','p',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','b',' ','F',' ',' ','b',' ','S',' ','b',' ',' ',' ',' ',' ',' ',' '},
				{' ','c',' ',' ',' ',' ',' ',' ',' ',' ',' ','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','b','p','p','p',' ',' ',' ',' ',' ',' ',' ',' '},
				{' ',' ',' ',' ',' ',' ','p',' ',' ','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','p','p',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
				{' ','P',' ',' ',' ',' ',' ',' ',' ',' ','b',' ',' ',' ',' ',' ',' ','S',' ','B',' ',' ',' ',' ',' ','S',' ',' ',' ','S',' ',' ','B',' ',' ',' ','s',' ','b',' ',' ',' ',' ','b',' ','s','b',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','D',' '},
				{'G','G','G','G','G','G','W','W','W','W','W','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','W','W','G','G','W','W','G','G','W','W','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G','G'}
		};
		
		LevelGenerator generator = new LevelGenerator(levelView);
		Level level = generator.generateLevel(levelInput);
		level.start();
		
		primaryStage.setScene(scene);
		
		level.wonLostObservable.subscribe(won -> {
			if(won) {
				scene = new Scene(new WonView(), 1000, 630);
			} else {
				scene = new Scene(new LostView(), 1000, 630);
			}
			primaryStage.setScene(scene);
			Observables.keyPress(scene, KeyCode.R).subscribe(event -> {
				startGame(primaryStage);
			});
		});
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}