package rx.platformer.model;

import javafx.animation.RotateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy extends MovingObject {

	private boolean hasDied;
	
	public Enemy(double xPos, ImageView view, int clockSpeed) {
		super(xPos, view, clockSpeed);
	}
	
	public void die() {
		hasDied = true;
		stopMoving();
		RotateTransition sc = new RotateTransition(Duration.millis(300), this.getView());
		sc.setToAngle(360);
		sc.play();
		sc.setOnFinished(f -> setPosX(Double.MAX_VALUE));
	}
	
	public boolean hasDied() {
		return hasDied;
	}

}
