package rx.rx2048;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import rx.subjects.PublishSubject;

/**
 *
 * @author bruno
 */
public class GameManager extends Group {

    private static final int FINAL_VALUE_TO_WIN = 2048;
    public static final int CELL_SIZE = 128;
    private static final int DEFAULT_GRID_SIZE = 4;
    private static final int BORDER_WIDTH = (14 + 2) / 2;
    // grid_width=4*cell_size + 2*cell_stroke/2d (14px css)+2*grid_stroke/2d (2 px css)
    private static final int GRID_WIDTH = CELL_SIZE * DEFAULT_GRID_SIZE + BORDER_WIDTH * 2;
    private static final int TOP_HEIGHT = 92;

    private volatile boolean movingTiles = false;
    private final int gridSize;
    private final List<Integer> traversalX;
    private final List<Integer> traversalY;
    private final List<Location> locations = new ArrayList<>();
    private final Map<Location, Tile> gameGrid;
    private final BooleanProperty gameWonProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOverProperty = new SimpleBooleanProperty(false);
    private final IntegerProperty gameScoreProperty = new SimpleIntegerProperty(0);
    private final Set<Tile> mergedToBeRemoved = new HashSet<>();
    private final ParallelTransition parallelTransition = new ParallelTransition();
    private final BooleanProperty layerOnProperty = new SimpleBooleanProperty(false);

    // User Interface controls
    private final VBox vGame = new VBox(50);
    private final Group gridGroup = new Group();

    private final HBox hTop = new HBox(0);
    private final Label lblScore = new Label("0");
    private final Label lblPoints = new Label();
    private final HBox hOvrLabel = new HBox();
    private final HBox hOvrButton = new HBox();

    public GameManager() {
        this(DEFAULT_GRID_SIZE);
    }

    public GameManager(int gridSize) {
        this.gameGrid = new HashMap<>();
        this.gridSize = gridSize;
        this.traversalX = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());
        this.traversalY = IntStream.range(0, gridSize).boxed().collect(Collectors.toList());

        createScore();
        createGrid();
        initGameProperties();

        initializeGrid();

        this.setManaged(false);
    }

    public void move(Direction direction) {
        if (this.layerOnProperty.get()) {
            return;
        }

        synchronized (this.gameGrid) {
            if (this.movingTiles) {
                return;
            }
        }
        
        //TODO replace Subject
        PublishSubject<Integer> pointPublisher = PublishSubject.create();
        pointPublisher.scan((i, sum) -> i + sum).last().filter(total -> total > 0)
        		.doOnNext(total -> this.animateScore(total.toString()).play())
        		.subscribe(total -> {}, exception -> {});
        		
        Collections.sort(this.traversalX, direction.getX() == 1 ? Collections.reverseOrder() : Integer::compareTo);
        Collections.sort(this.traversalY, direction.getY() == 1 ? Collections.reverseOrder() : Integer::compareTo);
        final int tilesWereMoved = traverseGrid((int x, int y) -> {
            Location thisloc = new Location(x, y);
            Tile tile = this.gameGrid.get(thisloc);
            if (tile == null) {
                return 0;
            }

            Location farthestLocation = findFarthestLocation(thisloc, direction); // farthest available location
            Location nextLocation = farthestLocation.offset(direction); // calculates to a possible merge
            Tile tileToBeMerged = nextLocation.isValidFor(this.gridSize) ? this.gameGrid.get(nextLocation) : null;

            if (tileToBeMerged != null && tileToBeMerged.getValue().equals(tile.getValue()) && !tileToBeMerged.isMerged()) {
                tileToBeMerged.merge(tile);

                this.gameGrid.put(nextLocation, tileToBeMerged);
                this.gameGrid.replace(tile.getLocation(), null);

                this.parallelTransition.getChildren().add(animateExistingTile(tile, tileToBeMerged.getLocation()));
                this.parallelTransition.getChildren().add(hideTileToBeMerged(tile));
                this.mergedToBeRemoved.add(tile);

                pointPublisher.onNext(tileToBeMerged.getValue());
                this.gameScoreProperty.set(this.gameScoreProperty.get() + tileToBeMerged.getValue());

                if (tileToBeMerged.getValue() == FINAL_VALUE_TO_WIN) {
                    this.gameWonProperty.set(true);
                }
                return 1;
            } else if (farthestLocation.equals(tile.getLocation()) == false) {
                this.parallelTransition.getChildren().add(animateExistingTile(tile, farthestLocation));

                this.gameGrid.put(farthestLocation, tile);
                this.gameGrid.replace(tile.getLocation(), null);

                tile.setLocation(farthestLocation);

                return 1;
            }

            return 0;
        });
        
        pointPublisher.onCompleted();
        
        this.parallelTransition.setOnFinished(e -> {
            synchronized (this.gameGrid) {
                this.movingTiles = false;
            }

            this.gridGroup.getChildren().removeAll(this.mergedToBeRemoved);

            // game is over if there is no more moves
            Location randomAvailableLocation = findRandomAvailableLocation();
            if (randomAvailableLocation == null && !mergeMovementsAvailable()) {
                this.gameOverProperty.set(true);
            } else if (randomAvailableLocation != null && tilesWereMoved > 0) {
                addAndAnimateRandomTile(randomAvailableLocation);
            }

            this.mergedToBeRemoved.clear();

            // reset merged after each movement
            this.gameGrid.values().stream().filter(Objects::nonNull).forEach(Tile::clearMerge);
        });

        synchronized (this.gameGrid) {
            this.movingTiles = true;
        }

        this.parallelTransition.play();
        this.parallelTransition.getChildren().clear();
    }

    private Location findFarthestLocation(Location location, Direction direction) {
        Location farthest;

        do {
            farthest = location;
            location = farthest.offset(direction);
        } while (location.isValidFor(this.gridSize) && this.gameGrid.get(location) == null);

        return farthest;
    }

    private int traverseGrid(IntBinaryOperator func) {
        AtomicInteger at = new AtomicInteger();
        this.traversalX.forEach(t_x -> {
            this.traversalY.forEach(t_y -> {
                at.addAndGet(func.applyAsInt(t_x, t_y));
            });
        });

        return at.get();
    }

    private boolean mergeMovementsAvailable() {
        final SimpleBooleanProperty foundMergeableTile = new SimpleBooleanProperty(false);

        Stream.of(Direction.UP, Direction.LEFT).parallel().forEach(direction -> {
            int mergeableFound = traverseGrid((x, y) -> {
                Location thisloc = new Location(x, y);
                Tile tile = this.gameGrid.get(thisloc);

                if (tile != null) {
                    Location nextLocation = thisloc.offset(direction); // calculates to a possible merge
                    if (nextLocation.isValidFor(this.gridSize)) {
                        Tile tileToBeMerged = this.gameGrid.get(nextLocation);
                        if (tile.isMergeable(tileToBeMerged)) {
                            return 1;
                        }
                    }
                }

                return 0;
            });

            if (mergeableFound > 0) {
                foundMergeableTile.set(true);
            }
        });

        return foundMergeableTile.getValue();
    }

    private void createScore() {
        Label lblTitle = new Label("2048");
        lblTitle.getStyleClass().add("title");
        Label lblSubtitle = new Label("FX");
        lblSubtitle.getStyleClass().add("subtitle");
        HBox hFill = new HBox();
        HBox.setHgrow(hFill, Priority.ALWAYS);
        hFill.setAlignment(Pos.CENTER);
        VBox vScore = new VBox();
        vScore.setAlignment(Pos.CENTER);
        vScore.getStyleClass().add("vbox");
        Label lblTit = new Label("SCORE");
        lblTit.getStyleClass().add("titScore");
        this.lblScore.getStyleClass().add("score");
        this.lblScore.textProperty().bind(this.gameScoreProperty.asString());
        vScore.getChildren().addAll(lblTit, this.lblScore);

        this.hTop.getChildren().addAll(lblTitle, lblSubtitle, hFill, vScore);
        this.hTop.setMinSize(GRID_WIDTH, TOP_HEIGHT);
        this.hTop.setPrefSize(GRID_WIDTH, TOP_HEIGHT);
        this.hTop.setMaxSize(GRID_WIDTH, TOP_HEIGHT);

        this.vGame.getChildren().add(this.hTop);
        getChildren().add(this.vGame);

        this.lblPoints.getStyleClass().add("points");

        getChildren().add(this.lblPoints);
    }

    private void createGrid() {
        final double arcSize = CELL_SIZE / 6d;

        IntStream.range(0, this.gridSize)
                .mapToObj(i -> IntStream.range(0, this.gridSize).mapToObj(j -> {
                    Location loc = new Location(i, j);
                    this.locations.add(loc);

                    Rectangle rect2 = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                    rect2.setArcHeight(arcSize);
                    rect2.setArcWidth(arcSize);
                    rect2.getStyleClass().add("grid-cell");
                    return rect2;
                }))
                .flatMap(s -> s)
                .forEach(r -> this.gridGroup.getChildren().add((Node) r));
//                .forEach(gridGroup.getChildren()::add);

        this.gridGroup.getStyleClass().add("grid");
        this.gridGroup.setManaged(false);
        this.gridGroup.setLayoutX(BORDER_WIDTH);
        this.gridGroup.setLayoutY(BORDER_WIDTH);

        HBox hBottom = new HBox();
        hBottom.getStyleClass().add("backGrid");
        hBottom.setMinSize(GRID_WIDTH, GRID_WIDTH);
        hBottom.setPrefSize(GRID_WIDTH, GRID_WIDTH);
        hBottom.setMaxSize(GRID_WIDTH, GRID_WIDTH);

        hBottom.getChildren().add(this.gridGroup);

        this.vGame.getChildren().add(hBottom);
    }

    private void initGameProperties() {
        this.gameOverProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.layerOnProperty.set(true);
                this.hOvrLabel.getStyleClass().setAll("over");
                this.hOvrLabel.setMinSize(GRID_WIDTH, GRID_WIDTH);
                Label lblOver = new Label("Game over!");
                lblOver.getStyleClass().add("lblOver");
                this.hOvrLabel.setAlignment(Pos.CENTER);
                this.hOvrLabel.getChildren().setAll(lblOver);
                this.hOvrLabel.setTranslateY(TOP_HEIGHT + this.vGame.getSpacing());
                this.getChildren().add(this.hOvrLabel);

                this.hOvrButton.setMinSize(GRID_WIDTH, GRID_WIDTH / 2);
                Button bTry = new Button("Try again");
                bTry.getStyleClass().setAll("try");

                bTry.setOnTouchPressed(e -> resetGame());
                bTry.setOnAction(e -> resetGame());

                this.hOvrButton.setAlignment(Pos.CENTER);
                this.hOvrButton.getChildren().setAll(bTry);
                this.hOvrButton.setTranslateY(TOP_HEIGHT + this.vGame.getSpacing() + GRID_WIDTH / 2);
                this.getChildren().add(this.hOvrButton);
            }
        });

        this.gameWonProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.layerOnProperty.set(true);
                this.hOvrLabel.getStyleClass().setAll("won");
                this.hOvrLabel.setMinSize(GRID_WIDTH, GRID_WIDTH);
                Label lblWin = new Label("You win!");
                lblWin.getStyleClass().add("lblWon");
                this.hOvrLabel.setAlignment(Pos.CENTER);
                this.hOvrLabel.getChildren().setAll(lblWin);
                this.hOvrLabel.setTranslateY(TOP_HEIGHT + this.vGame.getSpacing());
                this.getChildren().add(this.hOvrLabel);

                this.hOvrButton.setMinSize(GRID_WIDTH, GRID_WIDTH / 2);
                this.hOvrButton.setSpacing(10);
                Button bContinue = new Button("Keep going");
                bContinue.getStyleClass().add("try");
                bContinue.setOnAction(e -> {
                    this.layerOnProperty.set(false);
                    getChildren().removeAll(this.hOvrLabel, this.hOvrButton);
                });
                Button bTry = new Button("Try again");
                bTry.getStyleClass().add("try");
                bTry.setOnTouchPressed(e -> resetGame());
                bTry.setOnAction(e -> resetGame());
                this.hOvrButton.setAlignment(Pos.CENTER);
                this.hOvrButton.getChildren().setAll(bContinue, bTry);
                this.hOvrButton.setTranslateY(TOP_HEIGHT + this.vGame.getSpacing() + GRID_WIDTH / 2);
                this.getChildren().add(this.hOvrButton);
            }
        });
    }

    private void clearGame() {
        List<Node> collect = this.gridGroup.getChildren().filtered(c -> c instanceof Tile).stream().collect(Collectors.toList());
        this.gridGroup.getChildren().removeAll(collect);
        this.gameGrid.clear();
        getChildren().removeAll(this.hOvrLabel, this.hOvrButton);

        this.layerOnProperty.set(false);
        this.gameScoreProperty.set(0);
        this.gameWonProperty.set(false);
        this.gameOverProperty.set(false);

        initializeLocationsInGameGrid();
    }

    private void resetGame() {
        clearGame();
        initializeGrid();
    }

    /**
     * Clears the grid and redraws all tiles in the <code>gameGrid</code> object
     */
    private void redrawTilesInGameGrid() {
        this.gameGrid.values().stream().filter(Objects::nonNull).forEach(t -> {
            double layoutX = t.getLocation().getLayoutX(CELL_SIZE) - (t.getMinWidth() / 2);
            double layoutY = t.getLocation().getLayoutY(CELL_SIZE) - (t.getMinHeight() / 2);

            t.setLayoutX(layoutX);
            t.setLayoutY(layoutY);
            this.gridGroup.getChildren().add(t);
        });
    }

    private Timeline animateScore(String v1) {
        final Timeline timeline = new Timeline();
        this.lblPoints.setText("+" + v1);
        this.lblPoints.setOpacity(1);
        this.lblPoints.setLayoutX(400);
        this.lblPoints.setLayoutY(20);
        final KeyValue kvO = new KeyValue(this.lblPoints.opacityProperty(), 0);
        final KeyValue kvY = new KeyValue(this.lblPoints.layoutYProperty(), 100);

        Duration animationDuration = Duration.millis(600);
        final KeyFrame kfO = new KeyFrame(animationDuration, kvO);
        final KeyFrame kfY = new KeyFrame(animationDuration, kvY);

        timeline.getKeyFrames().add(kfO);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

    interface AddTile {

        void add(int value, int x, int y);
    }

    /**
     * Initializes all cells in gameGrid map to null
     */
    private void initializeLocationsInGameGrid() {
        traverseGrid((x, y) -> {
            Location thisloc = new Location(x, y);
            this.gameGrid.put(thisloc, null);
            return 0;
        });
    }

    private void initializeGrid() {
        initializeLocationsInGameGrid();

        Tile tile0 = Tile.newRandomTile();
        List<Location> randomLocs = new ArrayList<>(this.locations);
        Collections.shuffle(randomLocs);
        Iterator<Location> locs = randomLocs.stream().limit(2).iterator();
        tile0.setLocation(locs.next());

        Tile tile1 = null;
        if (new Random().nextFloat() <= 0.8) { // gives 80% chance to add a second tile
            tile1 = Tile.newRandomTile();
            if (tile1.getValue() == 4 && tile0.getValue() == 4) {
                tile1 = Tile.newTile(2);
            }
            tile1.setLocation(locs.next());
        }

        Arrays.asList(tile0, tile1).forEach(t -> {
            if (t == null) {
                return;
            }
            this.gameGrid.put(t.getLocation(), t);
        });

        redrawTilesInGameGrid();
    }

    /**
     * Finds a random location or returns null if none exist
     *
     * @return a random location or <code>null</code> if there are no more
     * locations available
     */
    private Location findRandomAvailableLocation() {
        List<Location> availableLocations = this.locations.stream().filter(l -> this.gameGrid.get(l) == null).collect(Collectors.toList());

        if (availableLocations.isEmpty()) {
            return null;
        }

        Collections.shuffle(availableLocations);
        Location randomLocation = availableLocations.get(new Random().nextInt(availableLocations.size()));
        return randomLocation;
    }

    private void addAndAnimateRandomTile(Location randomLocation) {
        Tile tile = Tile.newRandomTile();
        tile.setLocation(randomLocation);

        double layoutX = tile.getLocation().getLayoutX(CELL_SIZE) - (tile.getMinWidth() / 2);
        double layoutY = tile.getLocation().getLayoutY(CELL_SIZE) - (tile.getMinHeight() / 2);

        tile.setLayoutX(layoutX);
        tile.setLayoutY(layoutY);
        tile.setScaleX(0);
        tile.setScaleY(0);

        this.gameGrid.put(tile.getLocation(), tile);
        this.gridGroup.getChildren().add(tile);

        animateNewlyAddedTile(tile).play();
    }

    private static final Duration ANIMATION_EXISTING_TILE = Duration.millis(125);

    private Timeline animateExistingTile(Tile tile, Location newLocation) {
        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(tile.layoutXProperty(), newLocation.getLayoutX(CELL_SIZE) - (tile.getMinHeight() / 2));
        KeyValue kvY = new KeyValue(tile.layoutYProperty(), newLocation.getLayoutY(CELL_SIZE) - (tile.getMinHeight() / 2));

        KeyFrame kfX = new KeyFrame(ANIMATION_EXISTING_TILE, kvX);
        KeyFrame kfY = new KeyFrame(ANIMATION_EXISTING_TILE, kvY);

        timeline.getKeyFrames().add(kfX);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

    // after last movement on full grid, check if there are movements available
    private EventHandler<ActionEvent> onFinishNewlyAddedTile = e -> {
        if (this.gameGrid.values().parallelStream().noneMatch(Objects::isNull) && !mergeMovementsAvailable()) {
            this.gameOverProperty.set(true);
        }
    };

    private static final Duration ANIMATION_NEWLY_ADDED_TILE = Duration.millis(125);

    private Timeline animateNewlyAddedTile(Tile tile) {
        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(tile.scaleXProperty(), 1);
        KeyValue kvY = new KeyValue(tile.scaleYProperty(), 1);

        KeyFrame kfX = new KeyFrame(ANIMATION_NEWLY_ADDED_TILE, kvX);
        KeyFrame kfY = new KeyFrame(ANIMATION_NEWLY_ADDED_TILE, kvY);

        timeline.getKeyFrames().add(kfX);
        timeline.getKeyFrames().add(kfY);
        timeline.setOnFinished(this.onFinishNewlyAddedTile);
        return timeline;
    }

    private static final Duration ANIMATION_TILE_TO_BE_MERGED = Duration.millis(150);

    private Timeline hideTileToBeMerged(Tile tile) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(tile.opacityProperty(), 0);
        KeyFrame kf = new KeyFrame(ANIMATION_TILE_TO_BE_MERGED, kv);
        timeline.getKeyFrames().add(kf);
        return timeline;
    }

    public void saveSession() {
        SessionManager sessionManager = new SessionManager(DEFAULT_GRID_SIZE);
        sessionManager.saveSession(this.gameGrid, this.gameScoreProperty.getValue());
    }

    public void restoreSession() {
        SessionManager sessionManager = new SessionManager(DEFAULT_GRID_SIZE);

        clearGame();
        int score = sessionManager.restoreSession(this.gameGrid);
        if (score >= 0) {
            this.gameScoreProperty.set(score);
            redrawTilesInGameGrid();
        } else {
            // not session found, restart again
            resetGame();
        }
    }
}
