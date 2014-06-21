package rx.platformer.model;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.platformer.controller.FxScheduler;
import rx.subjects.PublishSubject;

public class MovingObject extends Observable<MovingObject> {
	private final PublishSubject<MovingObject> subject;
	private ImageView view;
	private double xPos;
	private Direction direction;
	private ConnectableObservable<Direction> timer;
	private Subscription timerSubscription;
	
	public MovingObject(double xPos, ImageView view, int clockSpeed) {
        this(PublishSubject.create(), xPos, view, clockSpeed);
    }
	
	private MovingObject(PublishSubject<MovingObject> subject, double xPos, ImageView view, int clockSpeed) {
		super(subscriber -> subject.subscribe(subscriber));
		this.subject = subject;
		
		this.view = view;
		this.xPos = xPos;
		
		direction = Direction.LEFT;
		
		subscribe(e -> Platform.runLater(() -> view.setTranslateX(e.getXPos())));
		
		timer = Observable.interval(clockSpeed, TimeUnit.MILLISECONDS)
					.map(i -> direction).publish();
	}
	
	public void startTimer() {
		timer.connect();
		timerSubscription = timer.subscribe(dir -> this.move(dir));
	}
	
	public ImageView getView() {
		return view;
	}
	
	public void setPosX(double xPos) {
		this.xPos = xPos;
		subject.onNext(this);
	}
	
	public void move(Direction direction) {
		if(direction == Direction.RIGHT) {
			setPosX(xPos+2);
		}
		else if (direction == Direction.LEFT) {
			setPosX(xPos-2);
		}
	}
	
	public double getXPos() {
		return xPos;
	}
	
	public void invertDirection() {
		if(direction == Direction.LEFT) {
			direction = Direction.RIGHT;
			view.setScaleX(-1);
		} else {
			direction = Direction.LEFT;
			view.setScaleX(1);
		}
	}
	
	public void stopMoving() {
		timerSubscription.unsubscribe();
	}
	
	public Observable<Direction> getTimer() {
		return timer;
	}
	
	public Direction getDirection() {
		return direction;
	}
}
