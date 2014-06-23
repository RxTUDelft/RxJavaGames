package rx.chainrx.model;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rx.Observable;
import rx.chainrx.controller.Observables;

public class CircleObject {
	private static Observable<Long> disappearTimer = Observables.disappearTimer();
	private Circle view;
	private CircleState state;
	private double x, y;
	private StackPane levelView;
	
	public CircleObject(Circle view, StackPane levelView) {
		this.levelView = levelView;
		this.view = view;
		this.state = CircleState.MOVING;
		
		this.levelView.getChildren().add(view);
	}
	
	public void expand() {
		state = CircleState.EXPANDED;
		ScaleTransition scaleTransition = new ScaleTransition(
				Duration.millis(500), view);
		scaleTransition.setByX(4);
		scaleTransition.setByY(4);
		scaleTransition.play();
		
		disappearTimer.subscribe(i -> disappear());
	}
	
	public void disappear() {
		ScaleTransition scaleTransition = new ScaleTransition(
				Duration.millis(500), view);
		scaleTransition.setToX(0);
		scaleTransition.setToY(0);
		scaleTransition.setOnFinished(event -> {
			state = CircleState.DISAPPEARED;
		});
		
		Platform.runLater(() -> 
			scaleTransition.play()
		);
	}
	
	public CircleState getState() {
		return state;
	}
	
	public Circle getView() {
		return view;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		
		view.setTranslateX(x);
		view.setTranslateY(y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
}
