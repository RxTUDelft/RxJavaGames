package rx.tudelft.pong.model;

import rx.tudelft.pong.model.ball.Ball;
import rx.tudelft.pong.model.paddle.Paddle;

public class GameState {

	private final Paddle player1;
	private final Paddle player2;
	private final Ball ball;

	public GameState(Paddle player1, Paddle player2, Ball ball) {
		this.player1 = player1;
		this.player2 = player2;
		this.ball = ball;
	}

	public GameState() {
		this(new Paddle(), new Paddle(), new Ball());
	}

	public Paddle getPlayer1() {
		return this.player1;
	}

	public Paddle getPlayer2() {
		return this.player2;
	}
	
	public Ball getBall() {
		return this.ball;
	}
}