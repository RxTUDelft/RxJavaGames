package rx.platformer.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.platformer.controller.Observables;
import rx.platformer.view.Images;
import rx.platformer.view.LevelView;
import rx.subjects.PublishSubject;

public class Level {
	public final PublishSubject<Boolean> wonLostObservable = PublishSubject.create();
	private boolean gameFinished;
	private LevelView levelView;
	private double totalWidth;
	private ConnectableObservable<Integer> clock;
	private Player player;
	private Direction horizontalDirection;
	private Set<ImageView> grounds; //items on which you can walk
	private Optional<Double> groundBelowY; // upper y coordinate of the ground below the player
	private boolean jumping; //jump key pressed
	private Set<ImageView> solidBlocks; //solid items you cannot walk through
	private Set<ImageView> keys; //keys to unlock the door
	private int collectedKeys;
	private Set<MovingObject> movingObjects;
	private Set<MovingObject> movingPlatforms;
	private Set<ImageView> movingObjectBlockers; //invisible blocks where enemies invert their direction
	private Set<Enemy> enemies;
	private Set<Subscription> subscriptions;
	
	public Level(LevelView levelView, double totalWidth) {
		this.levelView = levelView;
		this.totalWidth = totalWidth;
		
		subscriptions = new HashSet<Subscription>();
		clock = Observables.clock(10L);
		
		grounds = new HashSet<ImageView>();
		groundBelowY = Optional.empty();
		solidBlocks = new HashSet<ImageView>();
		keys = new HashSet<ImageView>();
		movingObjects = new HashSet<MovingObject>();
		movingPlatforms = new HashSet<MovingObject>();
		movingObjectBlockers = new HashSet<ImageView>();
		enemies = new HashSet<Enemy>();
		
		horizontalDirection = Direction.RESTING;
	}
	
	public void start() {
		clock.connect();
		startMovementObservables();
		startMovingObjectCollisionObservables();
		startEnemyCollisionObservables();
		
		for(MovingObject m : movingObjects) {
			m.startTimer();
		}
	}
	
	
	private void startMovingObjectCollisionObservables() {
		Set<ImageView> bounceObjects = new HashSet<ImageView>();
		bounceObjects.addAll(solidBlocks);
		bounceObjects.addAll(movingObjectBlockers);
		
		movingObjects.stream().forEach(e -> {
			subscriptions.add(
				clock.map(i -> bounceObjects.stream().anyMatch(s -> getMyBounds(s).intersects(getMyBounds(e.getView()))))
					.distinctUntilChanged()
					.filter(b -> b == true)
					.subscribe(b -> e.invertDirection())
			);
		});
	}

	private void startEnemyCollisionObservables() {
		enemies.stream().forEach(e -> {
			subscriptions.add(
				clock.filter(i -> ! e.hasDied())
					.map(i -> getMyBounds(e.getView()).intersects(getMyBounds(player.getView())))
					.distinctUntilChanged()
					.filter(b -> b == true)
					.map(b -> getMyBounds(player.getView()).getMaxY()-3 <= getMyBounds(e.getView()).getMinY())
					.subscribe(onTopOfEnemy -> {
						if(onTopOfEnemy) {
							e.die();
						} else {
							finishGame(false);
						}
					})
			);
		});
	}
	
	private void finishGame(boolean won) {
		if(! gameFinished) {
			wonLostObservable.onNext(won);
			gameFinished = true;
			
			subscriptions.forEach(s -> s.unsubscribe());
			movingObjects.forEach(s -> s.stopMoving());
		}
	}
	
	private void startMovementObservables() {
		//horizontal movement
		Observable<Direction> leftKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.LEFT).map(event -> Direction.LEFT);
		Observable<Direction> rightKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.RIGHT).map(event -> Direction.RIGHT);
		Observable<Direction> leftKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.LEFT).filter(e -> horizontalDirection == Direction.LEFT).map(event -> Direction.RESTING);
		Observable<Direction> rightKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.RIGHT).filter(e -> horizontalDirection == Direction.RIGHT).map(event -> Direction.RESTING);
		Observable<Direction> horizontalMerged = Observable.merge(leftKeyPressed, rightKeyPressed, leftKeyReleased, rightKeyReleased);
		subscriptions.add(
			horizontalMerged.subscribe(dir -> {
				horizontalDirection = dir;
			})
		);
		
		clock.map(i -> horizontalDirection)
			.filter(dir -> dir != Direction.RESTING)
			.filter(dir -> ! solidBlocks.stream().anyMatch(block -> getMyBounds(block).intersects(getBoundsAtDirection(dir, player.getView()))))
			.subscribe(dir -> {
				setPlayerSprite();
				move(dir);
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
		
		subscriptions.add(
			groundBelow.subscribe(groundsBelowPlayer -> {
				groundBelowY = groundsBelowPlayer.stream().min(Comparator.naturalOrder());
				player.setOnTheGround(groundBelowY.isPresent() && groundBelowY.get() == player.getY());
				setPlayerSprite();
			})
		);
		
		
		Observable<KeyEvent> zKeyPressed = Observables.keyPress(levelView.getScene(), KeyCode.Z);
		subscriptions.add(zKeyPressed.subscribe(e -> jumping = true));
		Observable<KeyEvent> zKeyReleased = Observables.keyRelease(levelView.getScene(), KeyCode.Z);
		subscriptions.add(zKeyReleased.subscribe(e -> jumping = false));
		
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
		
		subscriptions.add(
			yPos.subscribe(y -> {
				if(y > levelView.getHeight()) {
					finishGame(false);
				}
				player.setPosition(player.getX(), y);
			})
		);
	}

	private void move(Direction dir) {
		double halfSceneWidth = levelView.getScene().getWidth() / 2;
		if((dir == Direction.RIGHT && player.getX() <  halfSceneWidth) || 
				(dir == Direction.LEFT && levelView.getX() <= 0) ||
				(dir == Direction.LEFT && player.getX() > halfSceneWidth) ||
				(dir == Direction.RIGHT && halfSceneWidth + levelView.getX() >= totalWidth - halfSceneWidth)) {
			player.move(dir);
		} else {
			levelView.move(dir);
		}		
	}

	private void setPlayerSprite() {
		ImageView playerView = player.getView();
		switch (horizontalDirection) {
		case LEFT:
			if(player.isOnTheGround()) {
				playerView.setImage(Images.PLAYER_WALK);
			} else {
				playerView.setImage(Images.PLAYER_JUMP);
			}
			playerView.setScaleX(-1);
			break;
		case RIGHT:
			if(player.isOnTheGround()) {
				playerView.setImage(Images.PLAYER_WALK);
			} else {
				playerView.setImage(Images.PLAYER_JUMP);
			}
			playerView.setScaleX(1);
			break;
		default:
			player.getView().setImage(Images.PLAYER_FRONT);
			break;
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setDoor(ImageView door, Text txt) {
		txt.setVisible(false);
		subscriptions.add(
			clock.map(i -> getMyBounds(player.getView()).intersects(getMyBounds(door)))
				.distinctUntilChanged()
				.subscribe(collision -> {
					if(collision) {
						if(collectedKeys == keys.size()) {
							finishGame(true);
						}
						else {
							txt.setVisible(true);
						}
					} else {
						txt.setVisible(false);
					}
				})
		);
	}
	
	public void addGround(ImageView ground) {
		grounds.add(ground);
	}
	
	public void addSolidBlock(ImageView block) {
		solidBlocks.add(block);
	}
	
	public void addKey(ImageView key) {
		keys.add(key);
		
		subscriptions.add(
			clock.map(i -> getMyBounds(player.getView()).intersects(getMyBounds(key)))
				.filter(b -> b == true)
				.subscribe( b -> {
					key.setTranslateX(Double.MAX_VALUE);
					levelView.addCollectedKey();
					collectedKeys++;
				})
		);
	}
	
	public void addMovingObject(MovingObject movingObject) {
		movingObjects.add(movingObject);
	}
	
	public void addMovingPlatform(MovingObject platform) {
		movingPlatforms.add(platform);
		
		subscriptions.add(
			platform.getTimer().map(i -> getMyBounds(player.getView()).intersects(getMyBounds(platform.getView())) && 
					player.isOnTheGround())
				.filter(b -> b == true)
				.subscribe(b -> {
					Platform.runLater(() -> move(platform.getDirection()));
				})
		);
	}
	
	public void addMovingObjectBlocker(ImageView movingObjectBlocker) {
		movingObjectBlockers.add(movingObjectBlocker);
	}
	
	public void addEnemy(Enemy enemy) {
		enemies.add(enemy);
	}
	
	private Bounds getMyBounds(ImageView iv) {
		return iv.localToScene(iv.getLayoutBounds());
	}
	
	private Bounds getBoundsAtDirection(Direction dir, ImageView fromView) {
		Bounds playerBounds = getMyBounds(fromView);
		
		double minX = 0;
		
		if(dir == Direction.LEFT) {
			minX = playerBounds.getMinX() - 3;
		}
		else if (dir == Direction.RIGHT) {
			minX = playerBounds.getMaxX() + 2;
		}
		
		return new BoundingBox(minX, playerBounds.getMinY()+1, 1, playerBounds.getHeight()-2);
	}
}
