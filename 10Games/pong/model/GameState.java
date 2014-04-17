package model;

import java.util.Objects;

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
		Paddle paddle1 = this.player1.move(stepMillis.doubleValue() / 1000, inputs.getPlayer1());
		Paddle paddle2 = this.player2.move(stepMillis.doubleValue() / 1000, inputs.getPlayer2());
		Ball ball = this.stepBall(stepMillis);
		return new GameState(paddle1, paddle2, ball);
	}

	private Ball stepBall(Long stepMillis) {
		// double step = stepMillis.doubleValue() / 1000;
		//
		// if (Double.compare(0.0, this.ball.getPositionX() - GamePanel.paddleWidth * 2) >= 0) {
		// if (this.ball.getPositionY() > this.player1.getPosition() + GamePanel.paddleHeigth / 2
		// || this.ball.getPositionY() < this.player1.getPosition()
		// - GamePanel.paddleHeigth / 2) {
		// System.out.println("mis");
		// return this.ball.move(step, Direction.RESTING, Direction.RESTING);
		// }
		// else {
		// System.out.println("raak");
		// return this.ball.move(step, Direction.DOWN, Direction.RESTING);
		// }
		// }
		// else {
		// return this.ball.move(step, Direction.UP, Direction.RESTING);
		// // System.out.println(this.ball.getPositionX());
		// }
		return this.ball;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof GameState) {
			GameState that = (GameState) other;
			return Objects.equals(this.player1, that.player1)
					&& Objects.equals(this.player2, that.player2)
					&& Objects.equals(this.ball, that.ball);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.player1, this.player2, this.ball);
	}

	@Override
	public String toString() {
		return "<GameState[" + String.valueOf(this.player1) + ", " + String.valueOf(this.player2)
				+ ", " + String.valueOf(this.ball) + "]>";
	}
}
