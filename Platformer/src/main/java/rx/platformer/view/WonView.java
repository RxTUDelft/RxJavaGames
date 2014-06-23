package rx.platformer.view;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class WonView extends StackPane {
	HBox box;
	
	public WonView() {
		getStylesheets().add(getClass().getResource("/level_style.css").toExternalForm());
		setId("wonView");
		
		box = new HBox(30);
		box.setMaxSize(0, 0);
		
		ImageView sadPlayer = new ImageView(Images.PLAYER_HAPPY);
		box.getChildren().add(sadPlayer);
		
		Text txt = new Text("You won! :-)\nPress R to restart the game!");
		box.getChildren().add(txt);
		
		this.getChildren().add(box);
	}
}
