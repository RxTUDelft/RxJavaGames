package rx.tictactoe.controller;

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
		Images tile = t
				.getSprite()
				.map(s -> s == Sprite.O ? Images.OVAL : Images.CROSS)
				.orElse(Images.EMPTY);
		this.ui.setTile(x, y, tile);
	}
}
