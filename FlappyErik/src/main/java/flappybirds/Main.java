package flappybirds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch();
	}

	private final String jumpAudio = this.getClass().getResource("/smb3_jump.wav").toString();
	private final String coinAudio = this.getClass().getResource("/smb3_coin.wav").toString();
	private final String startAudio = this.getClass().getResource("/smb3_power-up.wav").toString();

	@Override
	public void start(Stage stage) {
		int screenWidth = 808;
		int screenHeight = 600;

		StackPane root = new StackPane();
		root.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(root);

		// time
		Observable<Integer> clock = Observable.timer(0, 16, TimeUnit.MILLISECONDS).map(x -> 1)
				.observeOn(new PlatformScheduler());

		// Gravity constant
		double gravity = 0.1;
		double jumpSpeed = 8.0;

		// Background
		Canvas sky = new Canvas(screenWidth, screenHeight);
		root.getChildren().add(sky);
		GraphicsContext context = sky.getGraphicsContext2D();
		context.setFill(Color.AZURE);
		context.fillRect(0, 0, screenWidth, screenHeight);

		// Grass
		Image grassTile = new Image("GrassBlock.png");
		double grassWidth = grassTile.getWidth();
		double grassHeight = grassTile.getHeight();
		int numberOfTiles = Double.valueOf(Math.ceil(screenWidth / grassWidth)).intValue() + 1;

		// Place tiles on bottom, spaced grassWidth apart
		List<ImageView> grass = new ArrayList<>();
		for (int i = 0; i < numberOfTiles; i++) {
			ImageView tile = new ImageView(grassTile);
			root.getChildren().add(tile);
			tile.setTranslateX(i * grassWidth);
			grass.add(tile);
		}

		clock.map(i -> 1).subscribe(v -> {
			grass.stream().forEach(tile -> {
				double dx = tile.getTranslateX();
				if (dx <= -grassWidth) {
					tile.setTranslateX(screenWidth - v);
				}
				else {
					tile.setTranslateX(dx - v);
				}
			});
		});

		// Heart
		Image heartTile = new Image("Heart.png");
		Image starTile = new Image("Star.png");
		ImageView heart = new ImageView(heartTile);
		root.getChildren().add(heart);
		heart.setTranslateY(200 - screenHeight);
		clock.map(i -> 3).subscribe(v -> {
			if (heart.getTranslateX() <= -heartTile.getWidth()) {
				heart.setTranslateX(screenWidth - v);
			}
			else {
				heart.setTranslateX(heart.getTranslateX() - v);
			}
		});

		// Bug
		Image bugTile = new Image("EnemyBug.png");
		ImageView bug = new ImageView(bugTile);
		Subject<Double, Double> jumps = PublishSubject.create();
		Observable<Double> velocity = jumps.flatMap(v0 -> clock.scan(v0, (v, x) -> v - gravity)
				.map(v -> v < -v0 ? 0 : v).distinctUntilChanged().takeUntil(jumps));

		root.getChildren().add(bug);
		double bugHomeY = (-grassHeight / 2) - 5;
		bug.setTranslateY(bugHomeY);
		bug.setTranslateX(screenHeight / 2);

		velocity.subscribe(dy -> bug.setTranslateY(bug.getTranslateY() - dy));

		KeyObservables.spaceBar(scene).filter(event -> bugHomeY - 1 <= bug.getTranslateY())
				.doOnEach(event -> new AudioClip(this.jumpAudio).play())
				.subscribe(event -> jumps.onNext(jumpSpeed));

		Observable<Bounds> heartPosition = clock.map(i -> heart.localToScene(heart
				.getLayoutBounds()));
		Observable<Bounds> bugPosition = clock.map(i -> bug.localToScene(bug.getLayoutBounds()));

		Observable
				.combineLatest(bugPosition, heartPosition,
						(Bounds bugBounds, Bounds heartBounds) -> bugBounds.intersects(heartBounds))
				.buffer(2, 1).filter(hits -> hits.get(0) != hits.get(1)).subscribe(hits -> {
					if (!hits.get(0)) {
						heart.setImage(starTile);
						new AudioClip(this.coinAudio).play();
					}
					if (!hits.get(1)) {
						heart.setImage(heartTile);
					}
				});

		stage.setOnShown(event -> new AudioClip(this.startAudio).play());

		stage.setTitle("FlappyErik");
		stage.setScene(scene);
		stage.show();
	}
}
