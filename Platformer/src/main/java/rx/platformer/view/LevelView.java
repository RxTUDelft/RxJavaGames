package rx.platformer.view;

import rx.platformer.model.Direction;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class LevelView extends StackPane {

	private Group scrollingObjects;
	private double x;
	
	public LevelView() {
		getStylesheets().add(getClass().getResource("/level_style.css").toExternalForm());
		setId("levelView");
		
		scrollingObjects = new Group();
		scrollingObjects.setManaged(false);
		this.getChildren().add(scrollingObjects);
	}
	
	public void addView(Node node, boolean isScrollable) {
		if(isScrollable) {
			scrollingObjects.getChildren().add(node);
		} else {
			this.getChildren().add(node);
		}
	}
	
	public void move(Direction direction) {
		if(direction == Direction.RIGHT) {
			x += 2;
		}
		else if (direction == Direction.LEFT) {
			x -= 2;
		}
		scrollingObjects.setTranslateX(-x);
	}
	
	public double getX() {
		return x;
	}
}
