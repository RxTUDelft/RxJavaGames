package rx.happer.view;

import rx.happer.model.GameSettings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StartScreen extends StackPane {
	ImageView iv;
	
	public StartScreen() {
		iv = new ImageView(new Image("startscreen.png"));
		iv.setFitWidth(GameSettings.gameSize);
		iv.setPreserveRatio(true);
		getChildren().add(iv);
	}
}
