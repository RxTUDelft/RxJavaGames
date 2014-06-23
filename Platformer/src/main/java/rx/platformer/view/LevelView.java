package rx.platformer.view;

import rx.platformer.model.Direction;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class LevelView extends StackPane {
	
	private Group scrollingObjects;
	private double x;
	private HBox collectedKeys;
	
	public LevelView() {
		getStylesheets().add(getClass().getResource("/level_style.css").toExternalForm());
		setId("levelView");
		
		scrollingObjects = new Group();
		scrollingObjects.setManaged(false);
		this.getChildren().add(scrollingObjects);
		
		collectedKeys = new HBox(10);
		this.getChildren().add(collectedKeys);
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
	
	public void addCollectedKey() {
		ImageView key = new ImageView(new Image("/key.gif", 0, 30, true, false));
		collectedKeys.getChildren().add(key);
	}
}
