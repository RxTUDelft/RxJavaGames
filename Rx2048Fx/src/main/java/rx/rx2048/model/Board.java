package rx.rx2048.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import rx.Observable;

public final class Board {

	private final int size;
	private final Observable<Location> locations;
	private final Map<Location, Optional<Tile>> gameGrid;

	public Board(int size) {
		this.size = size;
		this.locations = Observable.range(0, this.size)
				.flatMap(i -> Observable.range(0, this.size)
						.<Location> map(j -> new Location(i, j)));

		// creating grid
		this.gameGrid = new HashMap<>(this.size * this.size);
		this.locations.doOnNext(location -> this.gameGrid.put(location, Optional.empty()))
				.subscribe();
	}

	public int getSize() {
		return this.size;
	}

	public Observable<Location> getLocations() {
		return this.locations;
	}

	public Observable<Location> getAvailableLocations() {
		return this.locations.filter(location -> !this.getTileAt(location).isPresent());
	}

	public Optional<Tile> getTileAt(Location loc) {
		return this.gameGrid.get(loc);
	}

	public void removeTileAt(Location loc) {
		this.gameGrid.replace(loc, Optional.empty());
	}

	public void add(Tile tile) {
		this.gameGrid.put(tile.getLocation(), Optional.of(tile));
	}

	public void clear() {
		this.gameGrid.keySet()
				.stream()
				.forEach(this::removeTileAt);
	}

	public void clearMerge() {
		this.gameGrid.values()
				.stream()
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(Tile::clearMerge);
	}
	
	public boolean isFull() {
		return this.gameGrid.values().parallelStream().allMatch(Optional::isPresent);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Board) {
			Board that = (Board) other;
			return Objects.equals(this.size, that.size)
					&& Objects.equals(this.locations, that.locations)
					&& Objects.equals(this.gameGrid, that.gameGrid);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.size, this.locations, this.gameGrid);
	}

	@Override
	public String toString() {
		return "<Board[" + this.gameGrid.values()
				.stream()
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(Tile::toString)
				.collect(Collectors.joining(", ")) + "]>";
	}
}
