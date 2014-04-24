package rx.pong.model;

import java.util.Objects;

public class Inputs {

	private final Direction player1;
	private final Direction player2;

	public Inputs(Direction player1, Direction player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public Direction getPlayer1() {
		return this.player1;
	}

	public Direction getPlayer2() {
		return this.player2;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Inputs) {
			Inputs that = (Inputs) other;
			return Objects.equals(this.player1, that.player1)
					&& Objects.equals(this.player2, that.player2);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "<Inputs[" + String.valueOf(this.player1) + ", " + String.valueOf(this.player2) + "]>";
	}
}
