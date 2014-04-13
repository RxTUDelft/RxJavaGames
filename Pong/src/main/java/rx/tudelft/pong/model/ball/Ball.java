package rx.tudelft.pong.model.ball;

import java.util.Observable;

public class Ball extends Observable {

	public static final double ballDiameter = 0.05;

	private double posX;
	private double posY;
	private double velX;
	private double velY;

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

	public void setPositionX(double posX) {
		this.posX = posX;

		this.setChanged();
		this.notifyObservers();
	}

	public double getPositionY() {
		return this.posY;
	}

	public void setPositionY(double posY) {
		this.posY = posY;

		this.setChanged();
		this.notifyObservers();
	}

	public double getVelocityX() {
		return this.velX;
	}

	public void setVelocityX(double velX) {
		this.velX = velX;

		this.setChanged();
		this.notifyObservers();
	}

	public double getVelocityY() {
		return this.velY;
	}

	public void setVelocityY(double velY) {
		this.velY = velY;

		this.setChanged();
		this.notifyObservers();
	}
}
