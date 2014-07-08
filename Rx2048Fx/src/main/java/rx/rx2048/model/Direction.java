package rx.rx2048.model;

import java.util.NoSuchElementException;

import javafx.scene.input.KeyCode;

public enum Direction {

	UP(0, -1), DOWN(0, 1), RIGHT(1, 0), LEFT(-1, 0);
	
	private final int dx;
	private final int dy;

	private Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int getDx() {
		return this.dx;
	}
	
	
	public int getDy() {
		return this.dy;
	}

	@Override
	public String toString() {
		return "<Direction[" + this.name() + ", x=" + this.dx + ", y=" + this.dy + "]>";
	}

	public Direction goBack() {
		switch (this) {
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP:
				return DOWN;
			default:
				throw new NoSuchElementException();
		}
	}

	public static Direction valueOf(KeyCode key) {
		return valueOf(key.name());
	}
}
