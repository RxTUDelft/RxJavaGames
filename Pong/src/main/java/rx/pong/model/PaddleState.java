package rx.pong.model;

import java.util.Objects;

public class PaddleState {

	private final Paddle player1;
	private final Paddle player2;

	public PaddleState(Paddle player1, Paddle player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public PaddleState() {
		this(new Paddle(), new Paddle());
	}

	public Paddle getPlayer1() {
		return this.player1;
	}

	public Paddle getPlayer2() {
		return this.player2;
	}

	public PaddleState step(Long stepMillis, Inputs inputs) {
		Paddle paddle1 = this.player1.move(stepMillis.doubleValue() / 1000, inputs.getPlayer1());
		Paddle paddle2 = this.player2.move(stepMillis.doubleValue() / 1000, inputs.getPlayer2());
		return new PaddleState(paddle1, paddle2);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof PaddleState) {
			PaddleState that = (PaddleState) other;
			return Objects.equals(this.player1, that.player1)
					&& Objects.equals(this.player2, that.player2);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.player1, this.player2);
	}

	@Override
	public String toString() {
		return "<GameState[" + String.valueOf(this.player1) + ", " + String.valueOf(this.player2)
				+ "]>";
	}
}
