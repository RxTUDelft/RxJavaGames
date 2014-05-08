package rx.tictactoe.controller;

import javafx.event.ActionEvent;
import rx.Observer;
import rx.tictactoe.model.IGameStateManager;

public class RestartButtonController implements Observer<ActionEvent> {

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
	public void onNext(ActionEvent t) {
		this.gameState.startNewGame();
	}
}
