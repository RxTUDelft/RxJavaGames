package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class StartScreen extends StackPane {
	ImageView iv;
	
	public StartScreen() {
		iv = new ImageView(new Image(getClass().getResourceAsStream("/images/startscreen.png")));
		getChildren().add(iv);
	}
}
