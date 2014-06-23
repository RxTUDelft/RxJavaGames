package rx.chainrx.model;

import javafx.scene.layout.StackPane;
import rx.chainrx.view.PlayerCircleView;

public class PlayerCircle extends CircleObject {
	
	public PlayerCircle(StackPane levelView) {
		super(new PlayerCircleView(), levelView);
		setPosition(levelView.getWidth() / 2 - getView().getRadius(), 
				levelView.getHeight() / 2 - getView().getRadius());
	}

	public void place(double x, double y) {
		getView().setRadius(5);
		getView().setId("playerCirclePlaced");
		setPosition(x - getView().getRadius(), y - getView().getRadius());
		expand();
	}
	
}
