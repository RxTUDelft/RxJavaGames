package rx.rx2048.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rx.Observable;
import rx.Subscription;
import rx.rx2048.Observables;
import rx.rx2048.manager.BoardManager;
import rx.rx2048.model.Board;
import rx.rx2048.model.Direction;

public class GameUI extends Application {
	
	// Model
	private Board board = new Board(Constants.DEFAULT_GRID_SIZE.get());
	
	// UI
	private final TopUI topUI = new TopUI();
	private final ScoreUI scoreUI = new ScoreUI();
	private final PointsUI pointsUI = new PointsUI();
	private final BoardUI boardUI = new BoardUI();
	private final Bounds gameBounds;
	private final Group ui;
	
	// Manager
	private BoardManager boardManager = new BoardManager(this.board, this.boardUI);
	
	public GameUI() {
		super();
		
		this.topUI.getChildren().add(this.scoreUI);
		this.ui = new Group(new VBox(50, this.topUI, this.boardUI), this.pointsUI);
		
		this.gameBounds = this.ui.getLayoutBounds();
	}

	@Override
	public void start(Stage stage) {
		StackPane root = new StackPane(this.ui);
		root.setPrefSize(this.gameBounds.getWidth(), this.gameBounds.getHeight());
		ChangeListener<Number> resize = (ov, vOld, vNew) -> {
			this.ui.setLayoutX((root.getWidth() - this.gameBounds.getWidth()) / 2);
			this.ui.setLayoutY((root.getHeight() - this.gameBounds.getHeight()) / 2);
		};
		root.widthProperty().addListener(resize);
		root.heightProperty().addListener(resize);
		
		Scene scene = new Scene(root, 600, 720);
		scene.getStylesheets().add("UILayout.css");
		
		Observable<KeyCode> keyHandler = Observables.keyPress(scene)
				.map(KeyEvent::getCode);
		Observable<KeyCode> rHandler = keyHandler.filter(KeyCode.R::equals);
		Observable<Direction> direction = keyHandler.filter(KeyCode::isArrowKey)
				.map(Direction::valueOf);

		Subscription boardSubscription = direction.subscribe(this.boardManager);
		Subscription debugSubscription = rHandler.doOnNext(key -> System.out.println(this.boardUI.toString())).subscribe();
		
		// Just for testing purposed now
//		Observable<Points> points = direction.map(dir -> new Points(1));
//		points.subscribe(this.pointsUI);
//		points.scan(new Score(0), (s, p) -> new Score(s.getScore() + p.getPoints()))
//				.subscribe(this.scoreUI);
		
		// TODO:
//		stage.setFullScreen(true);
//		stage.setFullScreenExitHint("");
//		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		
		stage.setX(0);
		stage.setY(0);
		stage.setTitle("Rx2048Fx");
		stage.setScene(scene);
		stage.setMinWidth(this.gameBounds.getWidth());
		stage.setMinHeight(this.gameBounds.getHeight());
		stage.show();
		stage.setOnCloseRequest(we -> {
				boardSubscription.unsubscribe();
				debugSubscription.unsubscribe();
		});
	}

	public static void main(String[] args) {
		Application.launch();
	}
}
