package rx.platformer.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LostView extends StackPane {
	HBox box;
	
	public LostView() {
		getStylesheets().add(getClass().getResource("/level_style.css").toExternalForm());
		setId("lostView");
		
		box = new HBox(30);
		box.setMaxSize(0, 0);
		
		ImageView sadPlayer = new ImageView(Images.PLAYER_SAD);
		box.getChildren().add(sadPlayer);
		
		Text txt = new Text("You lost :-(\nPress R to restart the game!");
		box.getChildren().add(txt);
		
		this.getChildren().add(box);
	}
}
