package rx.tudelft.pong.model.paddle;

import java.util.Observable;

public class Inputs extends Observable {

	private Direction player1;
	private Direction player2;

	public Inputs(Direction player1, Direction player2) {
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}

	public Direction getPlayer1() {
		return this.player1;
	}

	public void setPlayer1(Direction player1) {
		this.player1 = player1;
		
		this.setChanged();
		this.notifyObservers();
	}

	public Direction getPlayer2() {
		return this.player2;
	}

	public void setPlayer2(Direction player2) {
		this.player2 = player2;
		
		this.setChanged();
		this.notifyObservers();
	}
}
