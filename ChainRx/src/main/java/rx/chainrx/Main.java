package rx.chainrx;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.chainrx.controller.Observables;
import rx.chainrx.model.Level;
import rx.chainrx.view.DialogMessage;

public class Main extends Application {
	private ArrayList<LevelSpecification> levels;
	private int currentLevel;
	private StackPane root;
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ChainRx");
		
		root = new StackPane();
		root.setPrefSize(800, 600);
		root.getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
		root.setId("root");
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		levels = new ArrayList<LevelSpecification>();
		
		//specify levels
		levels.add(new LevelSpecification(5, 1));
		levels.add(new LevelSpecification(7, 2));
		levels.add(new LevelSpecification(10, 3));
		levels.add(new LevelSpecification(15, 5));
		levels.add(new LevelSpecification(20, 8));
		levels.add(new LevelSpecification(25, 12));
		levels.add(new LevelSpecification(30, 17));
		levels.add(new LevelSpecification(35, 25));
		
		currentLevel = 1;
		
		Level.wonLostSubject.subscribe(won -> {
			DialogMessage dialog;
			
			if(won) {
				if(currentLevel < levels.size()) {
					currentLevel++;
					dialog = new DialogMessage("You win!", "Next level");
				} else {
					currentLevel = 1;
					dialog = new DialogMessage("You won the last level!", "Play again");
				}
			} else {
				dialog = new DialogMessage("You lose!", "Restart level");;
			}
			
			//add dialog to root
			StackPane sp = new StackPane();
			sp.setPrefSize(root.getWidth(), root.getHeight());
			sp.getChildren().add(dialog);
			root.getChildren().add(sp);
			
			Observables.mouseClick(dialog.getButton()).subscribe(event -> startLevel());
		});
		
		startLevel();
	}
	
	public void startLevel() {
		root.getChildren().clear();
		LevelSpecification ls = levels.get(currentLevel-1);
		new Level(root, currentLevel, ls.getNumberOfBalls(), ls.getBallsToExpand());
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
