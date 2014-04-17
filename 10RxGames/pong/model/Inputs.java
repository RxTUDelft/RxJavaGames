package model;

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
}
