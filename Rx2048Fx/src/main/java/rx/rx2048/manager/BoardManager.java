package rx.rx2048.manager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.rx2048.model.Board;
import rx.rx2048.model.Direction;
import rx.rx2048.model.Location;
import rx.rx2048.model.Tile;
import rx.rx2048.ui.BoardUI;
import rx.rx2048.ui.Constants;
import rx.rx2048.ui.TileUI;

public class BoardManager implements Observer<Direction> {

	private final Board board;
	private final BoardUI ui;

	private final List<Integer> traversalX;
	private final List<Integer> traversalY;

	public BoardManager(Board model, BoardUI ui) {
		this.board = model;
		this.ui = ui;

		this.traversalX = IntStream.range(0, this.board.getSize())
				.boxed()
				.collect(Collectors.toList());
		this.traversalY = IntStream.range(0, this.board.getSize())
				.boxed()
				.collect(Collectors.toList());

		this.initializeGrid();
	}

	public void initializeGrid() {
		this.findRandomAvailableLocations(2)
				.map(Tile::new)
				.doOnNext(tile -> this.ui.addTile(new TileUI(tile)))
				.subscribe(this.board::add);
	}
	
	private Observable<Location> findRandomAvailableLocations(int n) {
		return this.board.getAvailableLocations()
				.toList() // only 1 element coming through
				.doOnNext(Collections::shuffle)
				.map(list -> list.stream().limit(n).collect(Collectors.toList()))
				.flatMap(Observable::from); // n random locations coming through
	}

	@Override
	public void onCompleted() {
		System.out.println("Consummatum est");
	}

	@Override
	public void onError(Throwable e) {
		System.err.println(e.getMessage());
	}

	@Override
	public void onNext(Direction direction) {
		Collections.sort(this.traversalX, direction.getDx() == 1 ? Collections.reverseOrder()
				: Integer::compareTo);
		Collections.sort(this.traversalY, direction.getDy() == 1 ? Collections.reverseOrder()
				: Integer::compareTo);
		
		ParallelTransition parallelTransition = new ParallelTransition();
		Set<TileUI> mergedToBeRemoved = new HashSet<>();

		this.traverseGrid(direction)
				.map(thisLoc -> {
					Optional<Tile> optTile = this.board.getTileAt(thisLoc);
					if (!optTile.isPresent()) {
						return false;
					}
					Tile tile = optTile.get();
					
					Location farthestLocation = this.findFarthestLocation(thisLoc, direction);
					Location nextLocation = farthestLocation.move(direction);
					Optional<Tile> tileToBeMerged = nextLocation.isValidFor(this.board.getSize())
							? this.board.getTileAt(nextLocation)
							: Optional.empty();
							
					Func1<Optional<Boolean>, Optional<Boolean>> not = optB -> optB.map(b -> !b);
					Func2<Optional<Boolean>, Optional<Boolean>, Optional<Boolean>> and = (b1, b2) ->
							b1.flatMap(first -> b2.map(second -> Boolean.logicalAnd(first, second)));

					Optional<Boolean> b1 = tileToBeMerged.map(Tile::getValue)
							.map(tile.getValue()::equals);
					Optional<Boolean> b2 = not.call(tileToBeMerged.map(Tile::isMerged));
					
					if (and.call(b1, b2).orElse(false)) {
						Tile mergedTile = tileToBeMerged.map(ttbm -> ttbm.merge(tile)).orElseThrow(() -> new IllegalStateException("tileToBeMerged and tile can't be null"));
						assert mergedTile.getLocation().equals(nextLocation) : "mergedTile should have " + nextLocation + " as its location";
						
						this.board.add(mergedTile);
						this.board.removeTileAt(tile.getLocation());
						
						this.ui.getTileAt(mergedTile.getLocation())
								.ifPresent(mergedTileUI -> {
									mergedTileUI.setTile(mergedTile);
								});
						
						this.ui.getTileAt(tile.getLocation())
								.ifPresent(tileUI -> {
									parallelTransition.getChildren().addAll(this.animateExistingTile(tileUI, nextLocation),
											this.hideTileToBeMerged(tileUI));
									mergedToBeRemoved.add(tileUI);
								});
						
						return true;
					}
					else if (!farthestLocation.equals(tile.getLocation())) {
						Tile newTile = new Tile(tile.getValue(), farthestLocation);
						
						this.ui.getTileAt(tile.getLocation())
								.ifPresent(tileUI -> {
									parallelTransition.getChildren()
										.add(this.animateExistingTile(tileUI, farthestLocation));
									tileUI.setTile(newTile);
								});

						this.board.add(newTile);
						this.board.removeTileAt(tile.getLocation());
						return true;
					}
							
					return false;
				})
				.scan(false, (bool, prev) -> prev || bool)
				.last()
				.filter(b -> b) // only true allowed here; false would mean no moves done
				.doOnNext(bool -> { // so bool is always true
					parallelTransition.setOnFinished(e -> {
						mergedToBeRemoved.forEach(this.ui::removeTile);
						
						this.findRandomAvailableLocations(1)
								.map(Tile::new)
								.doOnNext(this.board::add)
								.map(TileUI::new)
								.doOnNext(tileUI -> {
									tileUI.setScaleX(0);
									tileUI.setScaleY(0);
								})
								.doOnNext(this.ui::addTile)
								.map(this::animateNewlyAddedTile)
								.subscribe(Timeline::play);
						
//						Observable<Location> randAvLoc = this.findRandomAvailableLocations(1);
//						Observable<Boolean> mergeMovementsAvailable = this.mergeMovementsAvailable();
//						Observable<Boolean> firstIf = Observable.combineLatest(randAvLoc.isEmpty(),
//								mergeMovementsAvailable,
//								(loc, merge) -> loc && !merge);
//						
//						firstIf.filter(b -> b)
//								.doOnNext(b -> System.out.println("1. game over!"))
//								.subscribe();
//						
//						firstIf.filter(b -> !b) // if firstIf is false then continue
//								.flatMap(b -> randAvLoc.isEmpty().<Boolean>map(noLoc -> !noLoc))
//								.filter(b -> b) // second if is true
//								.flatMap(b -> randAvLoc)
//								.map(Tile::new)
//								.doOnNext(this.board::add)
//								.map(TileUI::new)
//								.doOnNext(tileUI -> {
//									tileUI.setScaleX(0);
//									tileUI.setScaleY(0);
//								})
//								.doOnNext(this.ui::addTile)
//								.map(this::animateNewlyAddedTile)
//								.doOnNext(Timeline::play)
//								.subscribe();
						
						this.board.clearMerge();
					});
					
					parallelTransition.play();
				}).subscribe();
	}

	private Observable<Location> traverseGrid(Direction t) {
		return Observable.from(this.traversalX)
				.flatMap(x -> Observable.from(this.traversalY)
						.map(y -> new Location(x, y)));
	}

	private Location findFarthestLocation(Location location, Direction direction) {
		Location farthest;

		do {
			farthest = location;
			location = farthest.move(direction);
		}
		while (location.isValidFor(this.board.getSize())
				&& !this.board.getTileAt(location).isPresent());

		return farthest;
	}

	private Timeline animateExistingTile(TileUI tile, Location newLocation) {
		int cell = Constants.CELL_SIZE.get();
		Func1<Integer, Double> f = i -> i * cell + cell / 2 - tile.getMinHeight() / 2;
		
		Timeline timeline = new Timeline();
		KeyValue kvX = new KeyValue(tile.layoutXProperty(), f.call(newLocation.getX()));
		KeyValue kvY = new KeyValue(tile.layoutYProperty(), f.call(newLocation.getY()));
		
		KeyFrame kfX = new KeyFrame(Duration.millis(125), kvX);
		KeyFrame kfY = new KeyFrame(Duration.millis(125), kvY);
		
		timeline.getKeyFrames().add(kfX);
		timeline.getKeyFrames().add(kfY);
		return timeline;
	}
	
	private Timeline hideTileToBeMerged(TileUI tile) {
		Timeline timeline = new Timeline();
		KeyValue kv = new KeyValue(tile.opacityProperty(), 0);
		
		KeyFrame kf = new KeyFrame(Duration.millis(150), kv);
		
		timeline.getKeyFrames().add(kf);
		return timeline;
	}
	
	private Timeline animateNewlyAddedTile(TileUI tile) {
		Timeline timeline = new Timeline();
		KeyValue kvX = new KeyValue(tile.scaleXProperty(), 1);
		KeyValue kvY = new KeyValue(tile.scaleYProperty(), 1);

		KeyFrame kfX = new KeyFrame(Duration.millis(125), kvX);
		KeyFrame kfY = new KeyFrame(Duration.millis(125), kvY);

		timeline.getKeyFrames().add(kfX);
		timeline.getKeyFrames().add(kfY);
		return timeline;
		
		// after last movement on full grid, check if there are movements available
//		timeline.setOnFinished(e -> {
//			if (!this.board.isFull()) {
//				this.mergeMovementsAvailable()
//        				.filter(b -> !b)
//        				.doOnNext(b -> {
//        					// TODO game over!
//        					System.out.println("2. game over!");
//        				})
//        				.subscribe();
//			}
//		});
	}

//	private Observable<Boolean> mergeMovementsAvailable() {
//		return Observable.from(Direction.UP, Direction.LEFT)
//				.flatMap(direction -> this.traverseGrid(direction)
//						.<Boolean> map(thisLoc -> this.board.getTileAt(thisLoc)
//								.map(tile -> {
//									Location nextLocation = thisLoc.move(direction);
//									if (nextLocation.isValidFor(this.board.getSize())) {
//										return this.board.getTileAt(nextLocation)
//												.map(tile::isMergeable)
//												.orElse(false);
//									}
//									return false;
//								}).orElse(false)))
//				.scan(false, (bool, prev) -> prev || bool)
//				.last();
//	}
}
