package rx.tudelft.pong.model.paddle;

import java.util.Objects;

import rx.tudelft.pong.model.Direction;
import rx.tudelft.pong.ui.GamePanel;

public class Paddle {

	private final double position;

	public Paddle(double position) {
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
		//TODO make move independent from GamePanel dimensions
		switch (direction) {
			case RESTING:
				return new Paddle(this);
			case UP:
				return new Paddle(Math.max(GamePanel.paddleHeigth / 2, this.position - step));
			case DOWN:
				return new Paddle(Math.min(1.0 - GamePanel.paddleHeigth / 2, this.position + step));
			default:
				// TODO make optional?
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
