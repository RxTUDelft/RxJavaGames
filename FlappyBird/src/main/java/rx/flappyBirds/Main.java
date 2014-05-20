package rx.flappyBirds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class Main extends Application {

	private int highScore = 0;
	private int score = 0;

	private final String jumpAudio = this.getClass().getResource("/jump.wav").toString();
	private final String lostAudio = this.getClass().getResource("/lost.wav").toString();
	private final String scoreAudio = this.getClass().getResource("/score.wav").toString();
	private final String startAudio = this.getClass().getResource("/start.wav").toString();

	@Override
	public void start(Stage stage) {
		int screenWidth = 691;
		int screenHeight = 921;

		StackPane root = new StackPane();
		root.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(root, screenWidth, screenHeight);

		// time
		Observable<Integer> clock = Observable.timer(500, 10, TimeUnit.MILLISECONDS)
				.map(x -> 1)
				.observeOn(new FxScheduler());
		BehaviorSubject<Integer> scoreObservable = BehaviorSubject.create(this.score);
		BehaviorSubject<Integer> highScoreObservable = BehaviorSubject.create(this.highScore);

		// Constants
		double gravity = 0.1;
		double impuls = 4.0;

		// Background
		Image bgImg = new Image("background.png");
		ImageView background = new ImageView(bgImg);
		root.getChildren().add(background);
		background.setTranslateY(-115);

		// Bottom
		Image bottomImg = new Image("ground.png");
		double bottomWidth = bottomImg.getWidth();
		double bottomHeight = bottomImg.getHeight();
		int numberOfImgs = Double.valueOf(Math.ceil(screenWidth / bottomWidth)).intValue() + 1;

		List<ImageView> bottom = new ArrayList<>();
		for (int i = 0; i < numberOfImgs; i++) {
			ImageView tile = new ImageView(bottomImg);
			root.getChildren().add(tile);
			tile.setTranslateX(i * bottomWidth);
			bottom.add(tile);
		}

		Subscription bottomSubscription = clock.map(i -> 1)
				.subscribe(v ->
    				bottom.stream().forEach(tile -> {
    					double translateX = tile.getTranslateX();
    					if (translateX <= -bottomWidth) {
    						tile.setTranslateX(screenWidth - v);
    					}
    					else {
    						tile.setTranslateX(translateX - v);
    					}
    				}));

		// Pipes
		Image pipeImg = new Image("pipe.png");
		double pipeWidth = pipeImg.getWidth();
		double pipeDist = screenWidth * 2 / 3;
		List<ImageView[]> pipes = new ArrayList<>(3);
		int[] heights = { 100, 150, 200, 250, 300, 350, 400, 450, 500 };
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			ImageView pipeUp = new ImageView(pipeImg);
			ImageView pipeDown = new ImageView(pipeImg);
			root.getChildren().add(pipeUp);
			root.getChildren().add(pipeDown);
			pipeUp.setRotate(180.0);
			pipeUp.setTranslateX((i + 2) * pipeDist);
			pipeDown.setTranslateX((i + 2) * pipeDist);
			int h = heights[random.nextInt(heights.length)];
			pipeUp.setTranslateY(h - screenHeight);
			pipeDown.setTranslateY(h - screenHeight / 8);
			pipes.add(new ImageView[] { pipeUp, pipeDown });
		}

		Subscription pipeSubscription = clock.map(i -> 2)
				.subscribe(v ->
    				pipes.stream().forEach(array -> {
    					ImageView up = array[0];
    					ImageView down = array[1];
    					double dx = up.getTranslateX();
    					if (dx <= -pipeWidth) {
    						up.setTranslateX(3 * pipeDist - pipeWidth);
    						down.setTranslateX(3 * pipeDist - pipeWidth);
    						int h = heights[random.nextInt(heights.length)];
    						up.setTranslateY(h - screenHeight);
    						down.setTranslateY(h - screenHeight / 8);
    					}
						else {
							up.setTranslateX(dx - v);
							down.setTranslateX(dx - v);
						}
					}));

		// Score lines
		List<Line> lines = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			Line line = new Line(screenWidth / 2, -screenHeight, screenWidth / 2, 0);
			root.getChildren().add(line);
			line.setStroke(Color.TRANSPARENT);
			line.setTranslateX((i + 2.5) * pipeDist + pipeWidth / 2);
			lines.add(line);
		}

		Subscription scoreLineSubscription = clock.map(i -> 2)
				.subscribe(v ->
    				lines.stream().forEach(line -> {
    					double dx = line.getTranslateX();
    					if (dx <= pipeWidth) {
    						line.setTranslateX(3 * pipeDist + pipeWidth);
    					}
						else {
							line.setTranslateX(dx - v);
						}
					}));

		// Flappy
		Image flappyImg = new Image("Flappy.gif");
		Image flappyBoundImg = new Image("FlappyBounding.png");
		ImageView flappy = new ImageView(flappyImg);
		ImageView flappyB = new ImageView(flappyBoundImg);
		root.getChildren().add(flappyB);
		root.getChildren().add(flappy);
		double flappyInitY = -screenHeight / 2;
		flappy.setTranslateX(screenWidth / 4 - flappyImg.getWidth() / 2);
		flappyB.setTranslateX(36 + screenWidth / 4 - flappyImg.getWidth() / 2);
		flappyB.setScaleX(0.95);
		flappyB.setScaleY(0.95);

		Observable<List<KeyEvent>> spaceBarEvents = SpacebarObservable.spaceBar(scene)
				.distinctUntilChanged()
				.doOnEach(event -> new AudioClip(this.jumpAudio).play())
				.buffer(clock);
		Observable<Boolean> impulsForce = spaceBarEvents.map(list -> !list.isEmpty());
		Observable<Double> velocity = impulsForce.scan(0.0,
				(vOld, b) -> b ? impuls : vOld - gravity);
		Observable<Double> yPos = velocity.scan(flappyInitY,
				(yOld, dv) -> Math.min(yOld - dv, -bottomHeight));
		Subscription flappySubscription = yPos.subscribe(y -> {
			flappy.setTranslateY(y);
			flappyB.setTranslateY(y);
		});

		// Score system
		Label label = new Label();
		root.getChildren().add(label);
		StackPane.setMargin(label, new Insets(20, 0, 0, 0));
		StackPane.setAlignment(label, Pos.TOP_CENTER);
		label.setFont(Font.font("Tohama", FontWeight.BOLD, 50));
		label.setTextFill(Color.WHITE);
		label.setPadding(new Insets(5));
		label.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0, 2, 2);");
		Subscription scoreSubscription = scoreObservable.subscribe(i ->
				label.setText("score: " + i));
		
		// HighScore system
		Label highScoreLabel = new Label();
		root.getChildren().add(highScoreLabel);
		StackPane.setMargin(highScoreLabel, new Insets(90, 0, 0, 0));
		StackPane.setAlignment(highScoreLabel, Pos.TOP_CENTER);
		highScoreLabel.setFont(Font.font("Tohama", FontWeight.LIGHT, 25));
		highScoreLabel.setTextFill(Color.WHITE);
		highScoreLabel.setPadding(new Insets(5));
		highScoreLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 3, 0, 1, 1);");
		Subscription highScoreSubscription = highScoreObservable.subscribe(i ->
				highScoreLabel.setText("highscore: " + i));

		// Collision detection
		Func1<ImageView, Observable<Bounds>> func1Pipes = iv -> clock.map(i ->
				iv.localToScene(iv.getLayoutBounds()));
		List<Observable<Bounds>> pipeBounds = pipes.stream()
				.flatMap(ivs -> Arrays.stream(ivs))
				.map(iv -> func1Pipes.call(iv))
				.collect(Collectors.toList());

		Func1<Line, Observable<Bounds>> func1Lines = line -> clock.map(i ->
				line.localToScene(line.getLayoutBounds()));
		List<Observable<Bounds>> lineBounds = lines.stream()
				.map(line -> func1Lines.call(line))
				.collect(Collectors.toList());

		Observable<Bounds> flappyBounds = clock.map(i ->
				flappyB.localToScene(flappyB.getLayoutBounds()));

		pipeBounds.stream()
				.forEach(pipeBound -> Observable
						.combineLatest(pipeBound, flappyBounds, (p, f) -> p.intersects(f))
						.buffer(2, 1)
						.filter(hits -> hits.get(0) != hits.get(1))
						.subscribe(hits -> {
							if (!hits.get(0)) {
								bottomSubscription.unsubscribe();
								pipeSubscription.unsubscribe();
								scoreLineSubscription.unsubscribe();
								flappySubscription.unsubscribe();
								scoreSubscription.unsubscribe();
								highScoreSubscription.unsubscribe();

								new AudioClip(this.lostAudio).play();

								StackPane lostPane = new StackPane();
								root.getChildren().add(lostPane);
								lostPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");

								Label lostLabel = new Label("GAME OVER!!!");
								lostPane.getChildren().add(lostLabel);
								lostLabel
										.setStyle("-fx-background-color: rgb(255, 184, 16);\n"
												+ "-fx-background-radius: 40px;\n"
												+ "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.65), 10, 0, 10, 10);");
								lostLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 50));
								lostLabel.setPadding(new Insets(5, 10, 5, 10));
								lostLabel.setTextFill(Color.WHITE);
								
								SpacebarObservable.spaceBar(scene)
										.subscribe(event -> {
											this.score = 0;
											this.start(stage);
										});
							}
						}));

		lineBounds.stream()
				.forEach(line -> Observable
						.combineLatest(line, flappyBounds, (l, f) -> l.intersects(f))
						.buffer(2, 1)
						.filter(hits -> hits.get(0) != hits.get(1))
						.subscribe(hits -> {
							if (!hits.get(0)) {
								scoreObservable.onNext(++this.score);
								if (this.highScore <= this.score) {
									this.highScore = this.score;
									highScoreObservable.onNext(this.highScore);
								}
								new AudioClip(this.scoreAudio).play();
							}
						}));

		stage.setOnShown(event -> new AudioClip(this.startAudio).play());
		stage.setTitle("Flappy Bird");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}
}

