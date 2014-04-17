package model;

import java.util.Objects;

public class Ball {

	public static final double ballDiameter = 0.05;

	private final double posX;
	private final double posY;
	private final double velX;
	private final double velY;

	public Ball(double posX, double posY, double velX, double velY) {
		this.posX = posX;
		this.posY = posY;
		this.velX = velX;
		this.velY = velY;
	}

	public Ball() {
		this(0.5, 0.5, 0.5, 0.5);
	}

	public double getPositionX() {
		return this.posX;
	}

	public double getPositionY() {
		return this.posY;
	}

	public double getVelocityX() {
		return this.velX;
	}

	public double getVelocityY() {
		return this.velY;
	}

	public Ball move(double step, Direction xDir, Direction yDir) {
		double xPos = 0.0;
		double yPos = 0.0;

		switch (xDir) {
			case RESTING:
				xPos = this.posX;
				break;
			case UP:
				xPos = this.posX - this.velX * step;
				break;
			case DOWN:
				xPos = this.posX + this.velX * step;
				break;
			default:
				// TODO make optional?
				throw new IllegalArgumentException();
		}

		switch (yDir) {
			case RESTING:
				yPos = this.posY;
				break;
			case UP:
				yPos = this.posY - this.velY * step;
				break;
			case DOWN:
				yPos = this.posY + this.velY * step;
				break;
			default:
				// TODO make optional?
				throw new IllegalArgumentException();
		}

		return new Ball(xPos, yPos, this.velX, this.velY);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Ball) {
			Ball that = (Ball) other;
			return Objects.equals(this.posX, that.posX) && Objects.equals(this.posY, that.posY)
					&& Objects.equals(this.velX, that.velX) && Objects.equals(this.velY, that.velY);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.posX, this.posY, this.velX, this.velY);
	}

	@Override
	public String toString() {
		return "<Ball[" + this.posX + ", " + this.posY + ", " + this.velX + ", " + this.velY + "]>";
	}
}
