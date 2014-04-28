package rx.tictactoe.model;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import rx.Observer;

public class Game implements Observer<Tile> {

	private final Board board;

	public Game(int size) {
		this.board = new Board(size);
		this.board.subscribe(this);
	}

	public Board getBoard() {
		return this.board;
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
		System.out.println(this.detectWinningChain(t));
	}
	
	private Optional<Set<Tile>> detectWinningChain(Tile t) {
		Predicate<Tile> p = (tile) -> tile.getSprite() == t.getSprite();
		
		Set<Tile> row = this.board.getTilesInRowOf(t);
		if (row.stream().allMatch(p)) {
			return Optional.of(row);
		}
		
		Set<Tile> column = this.board.getTilesInColumnOf(t);
		if (column.stream().allMatch(p)) {
			return Optional.of(column);
		}
		
		Optional<Set<Tile>> diagUD = this.board.getTilesInDiagonalUpDown(t);
		if (diagUD.isPresent() && diagUD.get().stream().allMatch(p)) {
			return diagUD;
		}
		
		Optional<Set<Tile>> diagDU = this.board.getTilesInDiagonalDownUp(t);
		if (diagDU.isPresent() && diagDU.get().stream().allMatch(p)) {
			return diagDU;
		}
		
		return Optional.empty();
	}
}
