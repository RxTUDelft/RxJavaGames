package rx.rx2048.model;

import java.util.Objects;
import java.util.Random;

public final class Tile {

	private final Integer value;
	private final Location location;
	private boolean merged;
	
	public Tile(Location location) {
		this(new Random(), location);
	}

	public Tile(Random rand, Location location) {
		this(rand.nextDouble() < 0.9 ? 2 : 4, location);
	}

	public Tile(Integer value, Location location) {
		this(value, location, false);
	}
	
	public Tile(Integer value, Location location, boolean merged) {
		this.value = value;
		this.location = location;
		this.merged = merged;
	}

	public Integer getValue() {
		return this.value;
	}

	public Location getLocation() {
		return this.location;
	}

	public Tile merge(Tile other) {
		assert this.isMergeable(other);
		return new Tile(this.value + other.value, this.location, true);
	}
	
	public boolean isMerged() {
		return this.merged;
	}
	
	public void clearMerge() {
		this.merged = false;
	}

	public boolean isMergeable(Tile other) {
		return this.value.equals(other.value)
				&& this.location.getX() == other.location.getX()
				|| this.location.getY() == other.location.getY();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Tile) {
			Tile that = (Tile) other;
			return Objects.equals(this.value, that.value)
					&& Objects.equals(this.location, that.location);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.value, this.location, this.merged);
	}

	@Override
	public String toString() {
		return "<Tile[" + String.valueOf(this.value) + ", " + String.valueOf(this.location) + ", " + this.merged + "]>";
	}
}
