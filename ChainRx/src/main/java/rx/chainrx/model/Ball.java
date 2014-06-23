package rx.chainrx.model;

import java.util.Random;

import javafx.scene.layout.StackPane;
import rx.chainrx.view.BallView;

public class Ball extends CircleObject {
	private double velocityX, velocityY;
	private StackPane levelView;
	
	public Ball(StackPane levelView) {
		super(new BallView(), levelView);
		this.levelView = levelView;
		
		Random random = new Random();
		double startX = random.nextInt((int) (levelView.getWidth() - 2*getView().getRadius()));
		double startY = random.nextInt((int) (levelView.getHeight() - 2*getView().getRadius()));
		setPosition(startX, startY);
		
		double totalVelocity = 1.2;
		//random between -totalVelocity and totalVelocity
		this.velocityX = -totalVelocity + (2*totalVelocity) * random.nextDouble();
		double remainedVelocity = totalVelocity - Math.abs(velocityX);
		if(random.nextBoolean()) {
			this.velocityY = remainedVelocity;
		} else {
			this.velocityY = -remainedVelocity;
		}
		
	}
	
	public void move() {
		double newX = getX() + velocityX;
		double newY = getY() + velocityY;
		
		if(newX > levelView.getWidth()-2*getView().getRadius() || newX < 0) {
			velocityX = -velocityX;
		}
		if(newY > levelView.getHeight()-2*getView().getRadius() || newY < 0) {
			velocityY = -velocityY;
		}
		
		setPosition(newX, newY);
	}
}
