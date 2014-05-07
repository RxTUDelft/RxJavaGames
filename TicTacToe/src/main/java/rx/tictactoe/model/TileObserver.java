package rx.tictactoe.model;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import rx.Observer;

public class TileObserver implements Observer<Tile> {
	
	private final Game game;

	public TileObserver(Game game) {
		this.game = game;
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
		Optional<Sprite> sprite = t.getSprite();
		if (sprite.isPresent()) {
			Optional<Set<Tile>> chain = this.detectWinningChain(t);
			if (chain.isPresent()) {
				this.game.wonBy(sprite.get());
			}
			else {
				if (t.getSprite().map(s -> this.detectFullBoard()).orElse(false)) {
					this.game.draw();
				}
			}
		}
	}

	private Optional<Set<Tile>> detectWinningChain(Tile t) {
		Predicate<Tile> p = (tile) -> tile.getSprite().equals(t.getSprite());

		Set<Tile> row = this.game.getBoard().getTilesInRowOf(t);
		if (row.stream().allMatch(p)) {
			return Optional.of(row);
		}

		Set<Tile> column = this.game.getBoard().getTilesInColumnOf(t);
		if (column.stream().allMatch(p)) {
			return Optional.of(column);
		}

		Optional<Set<Tile>> diagUD = this.game.getBoard().getTilesInDiagonalUpDown(t);
		if (diagUD.isPresent() && diagUD.get().stream().allMatch(p)) {
			return diagUD;
		}

		Optional<Set<Tile>> diagDU = this.game.getBoard().getTilesInDiagonalDownUp(t);
		if (diagDU.isPresent() && diagDU.get().stream().allMatch(p)) {
			return diagDU;
		}

		return Optional.empty();
	}
	
	private boolean detectFullBoard() {
		return this.game.getBoard().isFull();
	}
}
