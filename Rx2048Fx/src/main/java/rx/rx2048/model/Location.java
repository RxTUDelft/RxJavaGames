package rx.rx2048.model;

import java.util.Objects;

public final class Location {

	private final int x;
	private final int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Location move(Direction dir) {
		return new Location(this.x + dir.getDx(), this.y + dir.getDy());
	}

	public boolean isValidFor(int gridSize) {
		return this.x >= 0 
				&& this.x < gridSize
				&& this.y >= 0
				&& this.y < gridSize;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Location) {
			Location that = (Location) other;
			return Objects.equals(this.x, that.x)
					&& Objects.equals(this.y, that.y);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y);
	}

	@Override
	public String toString() {
		return "<Location[x=" + this.x + ", y=" + this.y + "]>";
	}
}
