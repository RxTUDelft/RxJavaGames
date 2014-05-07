package rx.happer.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import rx.happer.model.Block;
import rx.happer.model.Box;
import rx.happer.model.Game;
import rx.happer.model.GameObject;
import rx.happer.model.GameSettings;
import rx.Observer;

public class GameView extends StackPane {
	Game game;
	Canvas canvas;

	public GameView(Game game) {
		this.game = game;
		
		setId("game");
		
		// draw grid
		canvas = new Canvas(GameSettings.gameSize, GameSettings.gameSize);
		if (GameSettings.grid) {
			drawGrid();
		}
		this.getChildren().add(canvas);

		//move observer
		Observer<GameObject> gameObjectObserver = new Observer<GameObject>() {

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(GameObject obj) {
				obj.getImageView().setTranslateX(
						obj.getSquare().getPosition().x * GameSettings.squareSize);
				obj.getImageView().setTranslateY(
						obj.getSquare().getPosition().y * GameSettings.squareSize);
				
			}
		};		
		
		// pacman
		ImageView pacmanView = game.getPacman().getImageView();
		pacmanView.setFitWidth(GameSettings.squareSize);
		pacmanView.setFitHeight(GameSettings.squareSize);
		this.getChildren().add(pacmanView);
		game.getPacman().subscribe(gameObjectObserver);
		
		// happer
		ImageView happerView = game.getHapper().getImageView();
		happerView.setFitWidth(GameSettings.squareSize);
		happerView.setFitHeight(GameSettings.squareSize);
		this.getChildren().add(happerView);
		game.getHapper().subscribe(gameObjectObserver);

		// boxes
		for (Box box : game.getBoxes()) {	
			ImageView boxView = box.getImageView();
			boxView.setFitWidth(GameSettings.squareSize);
			boxView.setFitHeight(GameSettings.squareSize);
			box.subscribe(gameObjectObserver);
			this.getChildren().add(boxView);
		}
		
		//blocks
		for (Block block : game.getBlocks()) {	
			ImageView boxView = block.getImageView();
			boxView.setFitWidth(GameSettings.squareSize);
			boxView.setFitHeight(GameSettings.squareSize);
			block.subscribe(gameObjectObserver);
			this.getChildren().add(boxView);
		}
		
	}

	public void drawGrid() {
		int nummberOfHorSquares = GameSettings.numberOfHorizontalSquares;
		int numberOfVertSquares = GameSettings.numberOfVerticalSquares;
		int squareSize = GameSettings.squareSize;
		GraphicsContext context = canvas.getGraphicsContext2D();

		for (int i = 0; i <= nummberOfHorSquares; i++) {
			context.strokeLine(i * squareSize, 0, i * squareSize, numberOfVertSquares
					* squareSize);
		}

		for (int i = 0; i <= numberOfVertSquares; i++) {
			context.strokeLine(0, i * squareSize, nummberOfHorSquares * squareSize, i
					* squareSize);
		}
	}

}
