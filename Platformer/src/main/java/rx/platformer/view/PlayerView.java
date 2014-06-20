package rx.platformer.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerView extends ImageView {
	private final Image img_front = new Image("/player_front.png");
	
	public PlayerView() {
		super();
		setId("player");
		setImage(img_front);
		setFitHeight(70);
		setPreserveRatio(true);
	}
	
	public void redraw(double x, double y) {
		setTranslateX(x - getBoundsInLocal().getWidth() / 2);
		setTranslateY(y);
	}
	
}
