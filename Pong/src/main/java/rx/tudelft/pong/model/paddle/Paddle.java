package rx.tudelft.pong.model.paddle;

public class Paddle {

	private final double position;

	public Paddle(double position) {
		this.position = position;
	}

	public Paddle() {
		this(0.5);
	}

	public double getPosition() {
		return this.position;
	}
}