package rx.tudelft.pong.model;

import rx.tudelft.pong.model.ball.Ball;
import rx.tudelft.pong.model.paddle.Direction;
import rx.tudelft.pong.model.paddle.Inputs;
import rx.tudelft.pong.model.paddle.Paddle;
import rx.tudelft.pong.ui.GamePanel;

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

	public GameState step(Long stepMillis, Inputs inputs) {
		stepPaddle(stepMillis, this.player1, inputs.getPlayer1());
		stepPaddle(stepMillis, this.player2, inputs.getPlayer2());
		stepBall(stepMillis, this.ball);
		
		return this;
	}

	private static void stepPaddle(Long stepMillis, Paddle paddle, Direction direction) {
		double step = stepMillis.doubleValue() / 1000;
		switch (direction) {
			case RESTING:
				break;
			case UP:
				paddle.setPosition(Math.max(GamePanel.paddleHeigth / 2, paddle.getPosition() - step));
				break;
			case DOWN:
				paddle.setPosition(Math.min(1.0 - GamePanel.paddleHeigth / 2, paddle.getPosition() + step));
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	private static void stepBall(Long stepMillis, Ball ball) {
		//TODO needs to be implemented; returns its input value for now.
	}
}