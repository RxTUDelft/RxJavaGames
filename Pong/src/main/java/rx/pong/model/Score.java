package rx.pong.model;

import java.util.Objects;

public class Score {

	private final int player1;
	private final int player2;

	public Score() {
		this(0, 0);
	}

	private Score(int i, int j) {
		this.player1 = i;
		this.player2 = j;
	}
	
	public int getScore1() {
		return this.player1;
	}

	public int getScore2() {
		return this.player2;
	}

	public Score incrPlayer1() {
		return new Score(this.player1 + 1, this.player2);
	}

	public Score incrPlayer2() {
		return new Score(this.player1, this.player2 + 1);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Score) {
			Score that = (Score) other;
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
		return "<Score[" + this.player1 + ", " + this.player2 + "]>";
	}
}
