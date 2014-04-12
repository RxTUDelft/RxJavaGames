package rx.tudelft.pong.model.ball;

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
}