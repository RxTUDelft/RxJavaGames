package rx.rx2048.model;

import java.util.Objects;

public final class Score {

	private final int score;

	public Score(int score) {
		this.score = score;
	}

	public int getScore() {
		return this.score;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Score) {
			Score that = (Score) other;
			return Objects.equals(this.score, that.score);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.score);
	}
	
	@Override
	public String toString() {
		return "<Score[" + this.score + "]>";
	}
}
