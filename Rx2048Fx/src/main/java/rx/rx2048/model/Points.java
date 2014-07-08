package rx.rx2048.model;

import java.util.Objects;

public final class Points {

	private final int points;

	public Points(int points) {
		this.points = points;
	}

	public int getPoints() {
		return this.points;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Points) {
			Points that = (Points) other;
			return Objects.equals(this.points, that.points);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.points);
	}
	
	@Override
	public String toString() {
		return "<Points[" + this.points + "]>";
	}
}
