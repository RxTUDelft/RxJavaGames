package rx.platformer.view;

import javafx.scene.image.ImageView;

public class PlayerView extends ImageView {
	
	public PlayerView() {
		super();
		setImage(Images.PLAYER_FRONT);
	}
	
	public void redraw(double x, double y) {
		setTranslateX(x - getBoundsInLocal().getWidth() / 2);
		setTranslateY(y - 10);
	}
	
}
