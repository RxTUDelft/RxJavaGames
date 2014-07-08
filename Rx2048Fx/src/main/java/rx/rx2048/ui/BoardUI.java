package rx.rx2048.ui;

import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import rx.Observable;
import rx.rx2048.model.Location;

public class BoardUI extends HBox {
	
	private static final int CELL = Constants.CELL_SIZE.get();
	private static final double ARC_SIZE = CELL / 6.0;
	
	private final Group gridGroup = new Group();

	public BoardUI() {
		super();
		Observable.range(0, Constants.DEFAULT_GRID_SIZE.get())
				.flatMap(i -> Observable.range(0, Constants.DEFAULT_GRID_SIZE.get())
						.map(j -> new Rectangle(i * CELL, j * CELL, CELL, CELL))
						.doOnNext(rect -> rect.setArcHeight(ARC_SIZE))
						.doOnNext(rect -> rect.setArcWidth(ARC_SIZE))
						.doOnNext(rect -> rect.getStyleClass().add("grid-cell")))
				.subscribe(this.gridGroup.getChildren()::add);
		
		this.gridGroup.getStyleClass().add("grid");
		this.gridGroup.setManaged(false);
		this.gridGroup.setLayoutX(Constants.BORDER_WIDTH.get());
		this.gridGroup.setLayoutY(Constants.BORDER_WIDTH.get());

		this.getStyleClass().add("backGrid");
		this.setMinSize(Constants.GRID_WIDTH.get(), Constants.GRID_WIDTH.get());
		this.setPrefSize(Constants.GRID_WIDTH.get(), Constants.GRID_WIDTH.get());
		this.setMaxSize(Constants.GRID_WIDTH.get(), Constants.GRID_WIDTH.get());

		this.getChildren().add(this.gridGroup);
	}

	public void addTile(TileUI tile) {
		this.gridGroup.getChildren().add(tile);
	}
	
	public Optional<TileUI> getTileAt(Location location) {
		return this.gridGroup.getChildren()
				.stream()
				.filter(n -> n instanceof TileUI)
				.map(n -> (TileUI) n)
				.filter(t -> t.getTile().getLocation().equals(location))
				.findFirst();
	}
	
	public void removeTile(TileUI tile) {
		this.gridGroup.getChildren().remove(tile);
	}
	
	@Override
	public String toString() {
		return this.gridGroup.getChildren().stream()
				.filter(n -> n instanceof TileUI)
				.map(n -> (TileUI) n)
				.map(t -> t.toString())
				.collect(Collectors.joining(", "));
	}
}
