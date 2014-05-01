package rx.tictactoe.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rx.tictactoe.controller.BoardController;
import rx.tictactoe.controller.CloseButtonController;
import rx.tictactoe.controller.FXObservable;
import rx.tictactoe.controller.GameStateController;
import rx.tictactoe.controller.RestartButtonController;
import rx.tictactoe.model.Board;
import rx.tictactoe.model.Game;
import rx.tictactoe.model.TileObserver;

public class TicTacToe extends Application {
	
	// Model
	private Board board = new Board(Constants.getSize());
	private Game game = new Game(this.board);
	private TileObserver to = new TileObserver(this.game);
	
	// UI
	private TitleLabel title = new TitleLabel("Tic Tac Toe");
	private BoardPane boardUI = new BoardPane(this.game);
	private Button restartButton = new Button("Restart");
	private Button quitButton = new Button("Quit");
	private ButtonPane buttons = new ButtonPane(this.restartButton, this.quitButton);
	private BorderedTitledPane turn = new BorderedTitledPane("Turn:", new TileUI());
	
	// Controller
	private RestartButtonController restartController = new RestartButtonController(this.game);
	private CloseButtonController closeController = new CloseButtonController();
	private GameStateController gameStateController = new GameStateController(this.turn);
	private BoardController boardController = new BoardController(this.boardUI);

	@Override
	public void start(Stage stage) {
		this.board.subscribe(this.to);
		FXObservable.mouseClick(this.restartButton).subscribe(this.restartController);
		FXObservable.mouseClick(this.quitButton).subscribe(this.closeController);
		this.game.startWith(this.game.getGameState()).subscribe(this.gameStateController);
		this.board.subscribe(this.boardController);
		
		int unit = Constants.getUnit();
		
		GridPane root = new GridPane();
		root.setHgap(unit);
		root.setVgap(unit / 3.0);
		root.setPadding(new Insets(unit / 4.0));
		Scene scene = new Scene(root);

		// Title text
		root.add(this.title, 0, 0, 4, 1);

		// Board
		root.add(this.boardUI, 0, 1, 3, 3);

		// Right
		AnchorPane right = new AnchorPane();
		right.getChildren().addAll(this.buttons, this.turn);
		AnchorPane.setBottomAnchor(this.turn, 0.0);
		AnchorPane.setRightAnchor(this.turn, 0.0);
		AnchorPane.setTopAnchor(this.buttons, 0.0);
		AnchorPane.setLeftAnchor(this.buttons, unit * 1.0);
		root.add(right, 3, 1, 1, 3);

		stage.setTitle("TicTacToe");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}
}
