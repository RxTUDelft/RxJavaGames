package rx.platformer.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.platformer.controller.Observables;
import rx.platformer.view.LevelView;

public class Level {
	private LevelView levelView;
	private ConnectableObservable<Integer> clock;
	private Player player;
	private Direction horizontalDirection;
	private Set<ImageView> grounds;
	private Optional<Double> groundBelowY;
	private boolean jumping;
	private Set<ImageView> keys; //keys to unlock the door
	
	public Level(LevelView levelView) {
		this.levelView = levelView;
		
		clock = Observables.clock(10L);
		
		grounds = new HashSet<ImageView>();
		groundBelowY = Optional.empty();
		keys = new HashSet<ImageView>();
	}
	
	public void start() {
		clock.connect();
		startMovementObservables();
	}
	
	private void startMovementObservables() {
		//horizontal movement
		horizontalDirection = Direction.RESTING;
		Observable<Direction> leftKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.LEFT).map(event -> Direction.LEFT);
		Observable<Direction> rightKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.RIGHT).map(event -> Direction.RIGHT);
		Observable<Direction> leftKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.LEFT).filter(e -> horizontalDirection == Direction.LEFT).map(event -> Direction.RESTING);
		Observable<Direction> rightKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.RIGHT).filter(e -> horizontalDirection == Direction.RIGHT).map(event -> Direction.RESTING);
		Observable<Direction> horizontalMerged = Observable.merge(leftKeyPressed, rightKeyPressed, leftKeyReleased, rightKeyReleased);
		horizontalMerged.subscribe(dir -> horizontalDirection = dir);
		
		double halfSceneWidth = levelView.getScene().getWidth() / 2;
		clock.map(i -> horizontalDirection)
			.filter(dir -> dir != Direction.RESTING)
			.subscribe(dir -> {
				if((dir == Direction.RIGHT && player.getX() <  halfSceneWidth) || 
						(dir == Direction.LEFT && levelView.getX() <= 0)) {
					player.move(dir);
				} else {
					levelView.move(dir);
				}
			});
		
		//vertical movement
		double gravity = 0.12;
		double impuls = 6.0;
		
		Observable<Set<Double>> groundBelow = clock.map(i -> grounds.stream().filter(g -> {
			Bounds groundBounds = getMyBounds(g);
			Bounds playerBounds = getMyBounds(player.getView());
			Bounds playerCenterBounds = new BoundingBox(player.getX()-5, 0, 11, levelView.getScene().getHeight());
			return playerBounds.getMaxY() <= groundBounds.getMinY() &&
					playerCenterBounds.intersects(groundBounds.getMinX(), 0, groundBounds.getWidth(), levelView.getScene().getHeight());
		})
		.map(iv -> getMyBounds(iv).getMinY() - 70)
		.collect(Collectors.toSet()));
		
		groundBelow.subscribe(grounds -> {
			groundBelowY = grounds.stream().min(Comparator.naturalOrder());
			player.setOnTheGround(groundBelowY.isPresent() && groundBelowY.get() == player.getY());
		});
		
		
		Observable<KeyEvent> zKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.Z);
		zKeyPressed.subscribe(e -> jumping = true);
		Observable<KeyEvent> zKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.Z);
		zKeyReleased.subscribe(e -> jumping = false);
		
		Observable<Boolean> impulsForce = clock.map(i -> jumping && player.isOnTheGround());
		
		Observable<Double> velocity = impulsForce.scan(0.0,
				(vOld, b) -> {
					if(b)
						return impuls;
					else if(player.isOnTheGround()) {
						return 0.0;
					}
					else {
						return vOld-gravity;
					}
				});
		
		Observable<Double> yPos = velocity.scan(player.getY(),
				(yOld, dv) -> Math.min(yOld - dv, groundBelowY.isPresent() ? groundBelowY.get() : Double.MAX_VALUE));
		
		yPos.subscribe(y -> {
			player.setPosition(player.getX(), y);
		});
		
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void addGround(ImageView ground) {
		grounds.add(ground);
	}
	
	public void addKey(ImageView key) {
		keys.add(key);
		
		clock.map(i -> getMyBounds(player.getView()).intersects(getMyBounds(key)))
			.filter(b -> b == true)
			.subscribe( b -> {
				key.setTranslateX(0);
				key.setTranslateY(0);
			});
	}
	
	private Bounds getMyBounds(ImageView iv) {
		return iv.localToScene(iv.getLayoutBounds());
	}
}
