package rx.pong;

import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.functions.Action2;
import rx.functions.Func1;
import rx.pong.model.Ball;
import rx.pong.model.Direction;
import rx.pong.model.Paddle;
import rx.pong.model.PaddleState;
import rx.pong.observables.FXObservable;
import rx.pong.ui.PongBackground;
import rx.pong.ui.PongBall;
import rx.pong.ui.PongBorder;
import rx.pong.ui.PongPaddle;

public class Main extends Application {

	private static final double ballSize = 20;
	public static final Long frameRateMillis = 10L;
	private static final int screenWidth = 800;
	private static final int screenHeight = 600;

	// observables
	private final Observable<Integer> clock = FXObservable.clock(Main.frameRateMillis,
			TimeUnit.MILLISECONDS);

	// model
	private Ball ball = new Ball();

	// ui
	private final PongPaddle leftPaddleUI = new PongPaddle(0.02 * Main.screenWidth,
			0.1 * Main.screenHeight, Color.WHITE);
	private final PongPaddle rightPaddleUI = new PongPaddle(0.02 * Main.screenWidth,
			0.1 * Main.screenHeight, Color.WHITE);
	private final PongBall ballUI = new PongBall(ballSize, Color.WHITE);

	// functions
	private final Func1<Paddle, Double> paddleYPos = (paddle) -> this.leftPaddleUI.getHeight() / 2
			- paddle.getPosition() * Main.screenHeight;
	private final Func1<Ball, Double> ballXPos = (ball) -> Main.screenWidth * ball.getPositionX()
			- this.ballUI.getRadius();
	private final Func1<Ball, Double> ballYPos = (ball) -> this.ballUI.getRadius()
			- Main.screenHeight * ball.getPositionY();
	private final Action2<PongPaddle, Paddle> setPaddleYPos = (ui, paddle) -> ui
			.setTranslateY(this.paddleYPos.call(paddle));
	private final Action0 setBallXPos = () -> this.ballUI.setTranslateX(this.ballXPos
			.call(this.ball));
	private final Action0 setBallYPos = () -> this.ballUI.setTranslateY(this.ballYPos
			.call(this.ball));

	@Override
	public void start(Stage stage) throws Exception {
		StackPane root = new StackPane();
		root.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(root);

		// Background
		root.getChildren().add(new PongBackground(Main.screenWidth, Main.screenHeight));

		//borders
		PongBorder bottomBorder = new PongBorder(Main.screenWidth, 1, Color.BLACK);
		root.getChildren().add(bottomBorder);
		bottomBorder.setTranslateX(0);
		bottomBorder.setTranslateY(1);

		PongBorder topBorder = new PongBorder(Main.screenWidth, 1, Color.BLACK);
		root.getChildren().add(topBorder);
		topBorder.setTranslateX(0);
		topBorder.setTranslateY(-Main.screenHeight);

		PongBorder leftBorder = new PongBorder(1, Main.screenHeight, Color.BLACK);
		root.getChildren().add(leftBorder);
		leftBorder.setTranslateX(-ballSize * 2);
		leftBorder.setTranslateY(0);

		PongBorder rightBorder = new PongBorder(1, Main.screenHeight, Color.BLACK);
		root.getChildren().add(rightBorder);
		rightBorder.setTranslateX(Main.screenWidth + ballSize * 2);
		rightBorder.setTranslateY(0);

		// Paddles
		root.getChildren().add(this.leftPaddleUI);
		root.getChildren().add(this.rightPaddleUI);
		this.rightPaddleUI.setTranslateX(Main.screenWidth - this.rightPaddleUI.getWidth());

		FXObservable.paddleController(scene, this.clock)
				.subscribe(new Observer<PaddleState>() {

					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onNext(PaddleState newState) {
						Main.this.setPaddleYPos.call(Main.this.leftPaddleUI, newState.getPlayer1());
						Main.this.setPaddleYPos.call(Main.this.rightPaddleUI, newState.getPlayer2());
					}
				});

		// Ball
		root.getChildren().add(this.ballUI);
		this.clock.subscribe(i -> {
			this.ball = this.ball.move(Main.frameRateMillis.doubleValue() / 1000);
			this.setBallXPos.call();
			this.setBallYPos.call();
		});

		// Ball interactions
		Observable<Bounds> bottomBorderBounds = FXObservable.getBounds(this.clock, bottomBorder);
		Observable<Bounds> topBorderBounds = FXObservable.getBounds(this.clock, topBorder);
		Observable<Bounds> leftBorderBounds = FXObservable.getBounds(this.clock, leftBorder);
		Observable<Bounds> rightBorderBounds = FXObservable.getBounds(this.clock, rightBorder);
		Observable<Bounds> leftPaddleBounds = FXObservable.getBounds(this.clock, this.leftPaddleUI);
		Observable<Bounds> rightPaddleBounds = FXObservable.getBounds(this.clock,
				this.rightPaddleUI);
		Observable<Bounds> ballBounds = FXObservable.getBounds(this.clock, this.ballUI);

		FXObservable.intersect(topBorderBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = this.ball.changeDirection(Direction.RESTING, Direction.DOWN);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});

		FXObservable.intersect(bottomBorderBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = this.ball.changeDirection(Direction.RESTING, Direction.UP);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});

		FXObservable.intersect(leftBorderBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = new Ball(this.ball);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});

		FXObservable.intersect(rightBorderBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = new Ball(this.ball);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});

		FXObservable.intersect(rightPaddleBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = this.ball.changeDirection(Direction.DOWN, Direction.RESTING);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});

		FXObservable.intersect(leftPaddleBounds, ballBounds)
				.subscribe(hits -> {
					if (!hits.get(0) || !hits.get(1)) {
						this.ball = this.ball.changeDirection(Direction.UP, Direction.RESTING);
						this.setBallXPos.call();
						this.setBallYPos.call();
					}
				});
		
		stage.setTitle("RxPong");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}
}
