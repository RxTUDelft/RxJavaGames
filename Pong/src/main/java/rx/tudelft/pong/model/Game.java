package rx.tudelft.pong.model;

import rx.tudelft.pong.model.ball.Ball;
import rx.tudelft.pong.model.paddle.Direction;
import rx.tudelft.pong.model.paddle.Inputs;
import rx.tudelft.pong.model.paddle.Paddle;

public class Game {

	public static final double paddleHeigt = 0.1;
	public static final double paddleWidth = 0.02;

	public static GameState step(Long stepMillis, GameState oldState, Inputs inputs) {
		return new GameState(stepPaddle(stepMillis, oldState.getPlayer1(), inputs.getPlayer1()),
						stepPaddle(stepMillis, oldState.getPlayer2(), inputs.getPlayer2()),
						stepBall(stepMillis, oldState.getBall()));
	}

	public static Paddle stepPaddle(Long stepMillis, Paddle paddle, Direction direction) {
		double step = stepMillis.doubleValue() / 1000;
		switch (direction) {
			case RESTING:
				return paddle;
			case UP:
				return new Paddle(Math.max(paddleHeigt / 2, paddle.getPosition() - step));
			case DOWN:
				return new Paddle(Math.min(1.0 - paddleHeigt / 2, paddle.getPosition() + step));
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public static Ball stepBall(Long stepMillis, Ball ball) {
		//TODO needs to be implemented; returns its input value for now.
		return ball;
	}
}
