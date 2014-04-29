package rx.tictactoe.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Board extends Observable<Tile> {

	private final int size;
	private final List<List<Tile>> tiles;

	public Board(int size) {
		this(size, PublishSubject.create());
	}

	private Board(int size, PublishSubject<Tile> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.size = size;
		this.tiles = new ArrayList<>(this.size);

		for (int x = 0; x < size; x++) {
			List<Tile> temp = new ArrayList<>(this.size);
			for (int y = 0; y < size; y++) {
				Tile tile = new Tile(x, y);
				temp.add(tile);
				tile.subscribe(subject);
			}
			this.tiles.add(temp);
		}
	}

	private boolean isWithinBorders(int x, int y) {
		return isWithinRange(x) && isWithinRange(y);
	}

	private boolean isWithinRange(int size) {
		return size >= 0 && size < this.size;
	}

	public int getSize() {
		return this.size;
	}

	public boolean set(Sprite s, int x, int y) {
		assert this.isWithinBorders(x, y);
		Tile tile = this.tiles.get(x).get(y);
		if (!tile.getSprite().isPresent()) {
			tile.setSprite(Optional.of(s));
			return true;
		}
		return false;
	}

	public void reset() {
		this.tiles.stream()
				.forEach(list -> list.stream()
						.forEach(tile -> tile.setSprite(Optional.empty())));
	}

	public Optional<Sprite> spriteAt(int x, int y) {
		assert this.isWithinBorders(x, y);

		return this.tiles.get(x).get(y).getSprite();
	}

	public Set<Tile> getTilesInRowOf(Tile t) {
		int y = t.getY();
		assert this.isWithinRange(y);

		return this.tiles.stream()
				.map(list -> list.get(y))
				.collect(Collectors.toSet());
	}

	public Set<Tile> getTilesInColumnOf(Tile t) {
		return new HashSet<>(this.tiles.get(t.getX()));
	}

	public Optional<Set<Tile>> getTilesInDiagonalUpDown(Tile t) {
		if (t.getX() == t.getY()) {
			Set<Tile> ud = new HashSet<>(this.size);
			for (int i = 0; i < this.size; i++) {
				ud.add(this.tiles.get(i).get(i));
			}
			return Optional.of(ud);
		}
		return Optional.empty();
	}

	public Optional<Set<Tile>> getTilesInDiagonalDownUp(Tile t) {
		if (t.getX() + t.getY() == this.size - 1) {
			Set<Tile> du = new HashSet<>(this.size);
			for (int i = 0; i < this.size; i++) {
				du.add(this.tiles.get(i).get(this.size - i - 1));
			}
			return Optional.of(du);
		}
		return Optional.empty();
	}
}
