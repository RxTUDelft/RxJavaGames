package rx.chainrx.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import rx.Subscription;
import rx.chainrx.controller.Observables;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;

public class Level {
	public static PublishSubject<Boolean> wonLostSubject = PublishSubject.create();
	private StackPane levelView;
	private Label playerInfo;
	private int levelNumber;
	private PlayerCircle playerCircle;
	private int numberOfBalls;
	private int ballsToExpand;
	private Set<Ball> balls;
	private int expandedBalls;
	private boolean circlePlaced;

	public Level(StackPane root, int levelNumber, int numberOfBalls,
			int ballsToExpand) {
		this.levelView = root;
		this.playerInfo = new Label();
		this.levelNumber = levelNumber;
		this.playerCircle = new PlayerCircle(levelView);
		this.circlePlaced = false;
		this.numberOfBalls = numberOfBalls;
		this.ballsToExpand = ballsToExpand;
		this.expandedBalls = 0;
		
		//add player info text box
		playerInfo.setId("playerInfo");
		levelView.getChildren().add(playerInfo);
		setPlayerInfo();
		
		//start the clock
		ConnectableObservable<Integer> clock = Observables.clock(10L);
		Subscription clockSubscription = clock.connect();
		
		//add the balls
		balls = new HashSet<Ball>();
		IntStream.range(0, this.numberOfBalls).forEach(i -> {
			Ball b = new Ball(levelView);
			balls.add(b);
			Observables.movingBalls(clock, b).subscribe(Ball::move);
		});
		
		// move player circle
		Observables.movingPlayerCircle(levelView, playerCircle).subscribe(event -> {
			playerCircle.setPosition(event.getX() - playerCircle.getView().getRadius(), event.getY() - playerCircle.getView().getRadius());
		});
		
		// set player circle on mouse click
		Subscription levelClickSubscription = Observables.mouseClick(levelView).subscribe(
				event -> {
					if(! circlePlaced) {
						circlePlaced = true;
						playerCircle.place(event.getX(), event.getY());
						Observables.collisions(clock, balls, playerCircle).subscribe(b -> expandBall(b));
					}
				});
		
		Observables.endLevel(clock, playerCircle, balls).subscribe(ended -> {
			levelClickSubscription.unsubscribe();
			clockSubscription.unsubscribe();
			boolean won = expandedBalls >= ballsToExpand;
			wonLostSubject.onNext(won);
		});
	}

	private void expandBall(Ball b) {
		expandedBalls++;
		setPlayerInfo();
		b.expand();
	}
	
	private void setPlayerInfo() {
		playerInfo.setText("Level " + levelNumber + " - Goal: " + ballsToExpand + "/" + numberOfBalls + "\n" +
							expandedBalls + " " + (expandedBalls==1 ? "ball" : "balls") + " expanded");
	}
}
