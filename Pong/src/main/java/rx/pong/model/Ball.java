package rx.pong.model;

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

	public Ball(Ball ball) {
		this.posX = 0.5;
		this.posY = 0.5;
		this.velX = ball.velX;
		this.velY = ball.velY;
	}

	public Ball() {
		this(0.5, 0.5, 0.3, 0.5);
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
	
	public Ball move(double step) {
		return new Ball(this.posX + this.velX * step, this.posY + this.velY * step, this.velX, this.velY);
	}
	
	public Ball changeDirection(Direction x, Direction y) {
		double vX;
		double vY;
		switch (x) {
			case DOWN:
				if (this.velX < 0) {
					vX = this.velX;
				}
				else {
					vX = -this.velX;
				}
				break;
			case RESTING:
				vX = this.velX;
				break;
			case UP:
				if (this.velX > 0) {
					vX = this.velX;
				}
				else {
					vX = -this.velX;
				}
				break;
			default:
				throw new IllegalArgumentException("State " + x + " is not known.");
		}
		
		switch (y) {
			case DOWN:
				if (this.velY < 0) {
					vY = this.velY;
				}
				else {
					vY = -this.velY;
				}
				break;
			case RESTING:
				vY = this.velY;
				break;
			case UP:
				if (this.velY > 0) {
					vY = this.velY;
				}
				else {
					vY = -this.velY;
				}
				break;
			default:
				throw new IllegalArgumentException("State " + x + " is not known.");
		}
		
		return new Ball(this.posX, this.posY, vX, vY);
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
