package rx.happer.controller;

import javafx.scene.control.Button;
import rx.happer.model.Game;
import rx.happer.view.GameView;
import rx.happer.view.MainFrame;
import rx.happer.view.Menu;
import rx.happer.view.SettingsView;
import rx.happer.view.StartScreen;

public class MenuController {

	MainFrame mainFrame;
	Menu menu;
	GameController gameController;

	public MenuController(MainFrame mainFrame, Menu menu) {
		this.mainFrame = mainFrame;
		this.menu = menu;
	}

	public void addStartListener(Button btn) {
		Observables.mouseClick(btn).subscribe(event -> {
			startGame();
		});
	}

	public void addStopListener(Button btn) {
		Observables.mouseClick(btn).subscribe(event -> {
			stopGame();
		});
	}

	public void addRestartListener(Button btn) {
		Observables.mouseClick(btn).subscribe(event -> {
			stopGame();
			startGame();
		});
	}

	public void addSettingsListener(Button btn) {
		Observables.mouseClick(btn).subscribe(event -> {
			mainFrame.setLeftPane(new SettingsView());
			setButtonsDisabled(false, false, true, true);
		});
	}

	private void setButtonsDisabled(boolean btnStart, boolean btnStop,
			boolean btnRestart, boolean btnSettings) {
		menu.getBtnStart().setDisable(btnStart);
		menu.getBtnStop().setDisable(btnStop);
		menu.getBtnRestart().setDisable(btnRestart);
		menu.getBtnSettings().setDisable(btnSettings);
	}

	private void startGame() {
		Game game = new Game();
		GameView gv = new GameView(game);
		gameController = new GameController(game, gv);

		mainFrame.setLeftPane(gv);
		gv.requestFocus();

		setButtonsDisabled(true, false, false, true);
	}

	private void stopGame() {
		if(gameController != null) {
			gameController.unsubscribeInterval();
		}
		mainFrame.setLeftPane(new StartScreen());
		setButtonsDisabled(false, true, true, false);
	}

}
