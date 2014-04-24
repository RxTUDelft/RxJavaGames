package rx.pong.model;

import java.util.Objects;

public class Paddle {

	public static final double paddleWidth = 0.02;
	public static final double paddleHeigth = 0.1;

	private final double position;

	public Paddle(double position) {
		if (position < Paddle.paddleHeigth / 2 || position > 1.0 - Paddle.paddleHeigth / 2) {
			throw new IllegalArgumentException("Illegal position for this paddle: " + position);
		}
		this.position = position;
	}

	private Paddle(Paddle p) {
		this.position = p.getPosition();
	}

	public Paddle() {
		this(0.5);
	}

	public double getPosition() {
		return this.position;
	}

	public Paddle move(double step, Direction direction) {
		// TODO make move independent from GamePanel dimensions
		switch (direction) {
			case RESTING:
				return new Paddle(this);
			case UP:
				return new Paddle(Math.min(1.0 - Paddle.paddleHeigth / 2, this.position + step));
			case DOWN:
				return new Paddle(Math.max(Paddle.paddleHeigth / 2, this.position - step));
			default:
				// TODO make monad?
				throw new IllegalArgumentException();
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Paddle) {
			Paddle that = (Paddle) other;
			return Objects.equals(this.position, that.position);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.position);
	}

	@Override
	public String toString() {
		return "<Paddle[" + this.position + "]>";
	}
}
