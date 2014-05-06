package rx.happer.view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import rx.happer.controller.MenuController;

public class Menu extends VBox {
	MainFrame mainFrame;
	MenuController menuController;
	Button btnStart, btnStop, btnRestart, btnSettings;
	
	public Menu(MainFrame frame) {
		setId("menu");
		setSpacing(20);
		
		mainFrame = frame;
		menuController = new MenuController(mainFrame, this);
		
		btnStart = new MenuButton("Start", true);
		menuController.addStartListener(btnStart);
		getChildren().add(btnStart);
		
		btnStop = new MenuButton("Stop", false);
		menuController.addStopListener(btnStop);
		getChildren().add(btnStop);
		
		btnRestart = new MenuButton("Restart", false);
		menuController.addRestartListener(btnRestart);
		getChildren().add(btnRestart);
		
		btnSettings = new MenuButton("Settings", true);
		menuController.addSettingsListener(btnSettings);
		getChildren().add(btnSettings);
	}

	
	public Button getBtnStart() {
		return btnStart;
	}

	public Button getBtnStop() {
		return btnStop;
	}

	public Button getBtnRestart() {
		return btnRestart;
	}

	public Button getBtnSettings() {
		return btnSettings;
	}
	
}
