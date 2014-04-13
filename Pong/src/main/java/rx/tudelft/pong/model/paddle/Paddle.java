package rx.tudelft.pong.model.paddle;

import java.util.Observable;

public class Paddle extends Observable {
	
	public static final double paddleHeigt = 0.1;
	public static final double paddleWidth = 0.02;

	private double position;

	public Paddle(double position) {
		this.position = position;
	}

	public Paddle() {
		this(0.5);
	}

	public double getPosition() {
		return this.position;
	}

	public void setPosition(double position) {
		this.position = position;

		this.setChanged();
		this.notifyObservers();
	}
}
