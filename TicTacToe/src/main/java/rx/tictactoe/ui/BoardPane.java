package rx.tictactoe.ui;

import javafx.scene.layout.GridPane;
import rx.tictactoe.controller.FXObservable;
import rx.tictactoe.model.Game;

public class BoardPane extends GridPane {
	
	private TileUI[][] tiles;

	public BoardPane(Game game) {
		super();
		this.setVgap(Constants.getUnit() / 6.0);
		this.setHgap(Constants.getUnit() / 6.0);
		this.setStyle("-fx-background-color: black;");
		
		int n = game.getBoard().getSize();
		this.tiles = new TileUI[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				TileUI tile = new TileUI();
				this.tiles[i][j] = tile;
				final int x = i;
				final int y = j;
				FXObservable.mouseClick(tile).subscribe(event -> game.performMove(x, y));
				
				this.setTile(i, j, Images.EMPTY);
				this.add(tile, x, y);
			}
		}
	}

	public void setTile(int x, int y, Images tile) {
		TileUI t = this.tiles[x][y];
		t.setImage(tile);
	}
}
