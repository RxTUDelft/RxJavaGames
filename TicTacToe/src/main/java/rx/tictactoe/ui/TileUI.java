package rx.tictactoe.ui;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TileUI extends StackPane {

	private final Rectangle r;
	private final StackPane stackPane = new StackPane();
	private final int unit = Constants.getUnit();
	
	public TileUI() {
		this.r = new Rectangle(3 * this.unit, 3 * this.unit, Color.WHITE);
		this.setAlignment(Pos.CENTER);
		this.getChildren().add(this.r);
		this.getChildren().add(this.stackPane);
	}
	
	public void setImage(Images im) {
		if (im.getImage() == null) {
			this.stackPane.getChildren().clear();
		}
		ImageView tile = new ImageView(im.getImage());
		tile.setFitHeight(3 * this.unit);
		tile.setFitWidth(3 * this.unit);
		this.stackPane.getChildren().add(tile);
	}
}
