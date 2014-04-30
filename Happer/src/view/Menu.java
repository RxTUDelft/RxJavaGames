package view;

import model.Game;
import controller.GameController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Menu extends VBox {
	MainFrame gameFrame;
	Button start, stop, restart, settings;
	
	public Menu(MainFrame frame) {
		setId("menu");
		setSpacing(20);
		
		gameFrame = frame;
		
		start = new Button("Start");
		start.setPrefWidth(100);
		start.setOnAction(new EventHandler<ActionEvent>() {	
			@Override
			public void handle(ActionEvent event) {
				Game game = new Game();
				GameView gv = new GameView(game);
				new GameController(game);
				
				gameFrame.setLeftPane(gv);
				start.setDisable(true);
				stop.setDisable(false);
				restart.setDisable(false);
				settings.setDisable(true);
			}
		});
		getChildren().add(start);
		
		stop = new Button("Stop");
		stop.setPrefWidth(100);
		stop.setDisable(true);
		stop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gameFrame.setLeftPane(new StartScreen());
				start.setDisable(false);
				stop.setDisable(true);
				restart.setDisable(true);
				settings.setDisable(false);
			}
		});
		getChildren().add(stop);
		
		restart = new Button("Restart");
		restart.setPrefWidth(100);
		restart.setDisable(true);
		restart.setOnAction(new EventHandler<ActionEvent>() {	
			@Override
			public void handle(ActionEvent event) {
				stop.fire();
				start.fire();
			}
		});
		getChildren().add(restart);
		
		settings = new Button("Settings");
		settings.setPrefWidth(100);
		settings.setOnAction(new EventHandler<ActionEvent>() {	
			@Override
			public void handle(ActionEvent event) {
				gameFrame.setLeftPane(new SettingsView());
				start.setDisable(false);
				stop.setDisable(false);
				restart.setDisable(true);
				settings.setDisable(true);
			}
		});
		getChildren().add(settings);
		
		getStylesheets().add(getClass().getResource("Menu.css").toExternalForm());
	}
}
