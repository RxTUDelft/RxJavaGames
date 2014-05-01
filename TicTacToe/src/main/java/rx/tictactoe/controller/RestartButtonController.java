package rx.tictactoe.controller;

import javafx.scene.input.MouseEvent;
import rx.Observer;
import rx.tictactoe.model.IGameStateManager;

public class RestartButtonController implements Observer<MouseEvent> {

	private IGameStateManager gameState;

	public RestartButtonController(IGameStateManager gameState) {
		this.gameState = gameState;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(MouseEvent t) {
		this.gameState.startNewGame();
	}
}
