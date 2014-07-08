package rx.rx2048.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import rx.rx2048.model.Tile;

public class TileUI extends Label {

	private Tile tile;

	public TileUI(Tile tile) {
		super();
		this.tile = tile;

		this.initSize();
		this.initText();

		this.setLayoutX(this.tile.getLocation().getX() * Constants.CELL_SIZE.get()
				+ Constants.CELL_SIZE.get() / 2
				- this.getMinWidth() / 2);
		this.setLayoutY(this.tile.getLocation().getY() * Constants.CELL_SIZE.get()
				+ Constants.CELL_SIZE.get() / 2
				- this.getMinHeight() / 2);
	}

	private void initSize() {
		int size = Constants.CELL_SIZE.get() - 13;
		this.setMinSize(size, size);
		this.setMaxSize(size, size);
		this.setPrefSize(size, size);
		this.setAlignment(Pos.CENTER);
	}

	private void initText() {
		this.setText(this.tile.getValue().toString());
		this.getStyleClass().add("tile-" + this.tile.getValue());
	}
	
	public void setTile(Tile tile) {
		this.getStyleClass().remove("tile-" + this.tile.getValue());
		this.tile = new Tile(tile.getValue(), tile.getLocation());
		this.initText();
	}

	public Tile getTile() {
		return this.tile;
	}

	@Override
	public String toString() {
		return "<TileUI[" + this.tile + ", " + String.valueOf(this.getStyleClass()) + "]>";
	}
}
