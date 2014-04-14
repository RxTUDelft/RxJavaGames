package rx.tudelft.pong.model.paddle;

import rx.subjects.PublishSubject;

public class Paddle {

	private double position;
	public final PublishSubject<Paddle> observable = PublishSubject.create();

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

		this.observable.onNext(this);
	}
}
