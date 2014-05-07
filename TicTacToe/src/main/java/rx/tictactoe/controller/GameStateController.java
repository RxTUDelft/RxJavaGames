package rx.tictactoe.controller;

import rx.Observer;
import rx.tictactoe.model.GameState;
import rx.tictactoe.model.Sprite;
import rx.tictactoe.ui.BorderedTitledPane;
import rx.tictactoe.ui.Images;
import rx.tictactoe.ui.TileUI;

public class GameStateController implements Observer<GameState> {

	private final BorderedTitledPane ui;

	public GameStateController(BorderedTitledPane ui) {
		this.ui = ui;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(GameState t) {
		switch (t) {
			case TURN_O:
				TileUI oval = new TileUI();
				oval.setImage(Images.OVAL);
				this.ui.setContent(oval);
				this.ui.setTextVisible(false);
				break;
			case TURN_X:
				TileUI cross = new TileUI();
				cross.setImage(Images.CROSS);
				this.ui.setContent(cross);
				this.ui.setTextVisible(false);
				break;
			case WON_O:
				this.ui.setWinningText(Sprite.O);
				this.ui.setTextVisible(true);
				break;
			case WON_X:
				this.ui.setWinningText(Sprite.X);
				this.ui.setTextVisible(true);
				break;
			case DRAW:
				this.ui.setDrawText();
				this.ui.setTextVisible(true);
			default:
				break;
		}
	}
}
