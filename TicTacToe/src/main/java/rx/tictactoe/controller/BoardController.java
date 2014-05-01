package rx.tictactoe.controller;

import java.util.Optional;

import rx.Observer;
import rx.tictactoe.model.Sprite;
import rx.tictactoe.model.Tile;
import rx.tictactoe.ui.BoardPane;
import rx.tictactoe.ui.Images;

public class BoardController implements Observer<Tile> {

	private final BoardPane ui;

	public BoardController(BoardPane ui) {
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
	public void onNext(Tile t) {
		int x = t.getX();
		int y = t.getY();
		Optional<Sprite> sprite = t.getSprite();
		if (sprite.isPresent()) {
			switch (sprite.get()) {
				case O:
					this.ui.setTile(x, y, Images.OVAL);
					break;
				case X:
					this.ui.setTile(x, y, Images.CROSS);
					break;
				default:
					break;
			}
		}
		else {
			this.ui.setTile(x, y, Images.EMPTY);
		}
	}
}
