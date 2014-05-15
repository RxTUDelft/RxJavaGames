package rx.flappyBirds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.Observable;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		int screenWidth = 691;
		int screenHeight = 921;

		StackPane root = new StackPane();
		root.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(root, screenWidth, screenHeight);

		// time
		Observable<Integer> clock = Observable.timer(0, 10, TimeUnit.MILLISECONDS)
				.map(x -> 1)
				.observeOn(new FxScheduler());

		// Constants
		double gravity = 0.1;
		double impuls = 4;

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

		clock.map(i -> 1).subscribe(v ->
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
		List<ImageView[]> pipes = new ArrayList<>();
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

		clock.map(i -> 2).subscribe(v ->
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

		// Flappy
		Image flappyImg = new Image("Flappy.gif");
		ImageView flappy = new ImageView(flappyImg);
		root.getChildren().add(flappy);
		double flappyInitY = -screenHeight / 2;
		flappy.setTranslateX(screenWidth / 4 - flappyImg.getWidth() / 2);
		flappy.setTranslateY(flappyInitY);
		
		Observable<List<KeyEvent>> spaceBarEvents = SpacebarObservable.spaceBar(scene).buffer(clock);
		Observable<Boolean> impulsForce = spaceBarEvents.map(list -> !list.isEmpty());
		Observable<Double> velocity = impulsForce.scan(0.0, (vOld, i) -> i ? impuls : vOld - gravity);
		Observable<Double> yPos = velocity.scan(flappyInitY, (yOld, dv) -> Math.min(yOld - dv, -bottomHeight));
		yPos.subscribe(y -> flappy.setTranslateY(y));

		stage.setTitle("Flappy Bird");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}
}
