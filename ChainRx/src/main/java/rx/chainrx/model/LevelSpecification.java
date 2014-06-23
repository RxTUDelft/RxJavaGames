package rx.chainrx.model;

public class LevelSpecification {
	private int numberOfBalls, ballsToExpand;

	public LevelSpecification(int numberOfBalls, int ballsToExpand) {
		this.numberOfBalls = numberOfBalls;
		this.ballsToExpand = ballsToExpand;
	}

	public int getNumberOfBalls() {
		return numberOfBalls;
	}

	public int getBallsToExpand() {
		return ballsToExpand;
	}
}
