package rx.platformer.model;

import rx.Observable;
import rx.platformer.view.PlayerView;
import rx.subjects.PublishSubject;

public class Player extends Observable<Player> {
	private final PublishSubject<Player> subject;
	private PlayerView view;
	private double x, y;
	private boolean onTheGround;
	
	public Player(double x, double y) {
        this(PublishSubject.create(), x, y);
    }
	
	private Player(PublishSubject<Player> subject, double x, double y) {
		super(subscriber -> subject.subscribe(subscriber));
		this.subject = subject;
		
		view = new PlayerView();
		subscribe(p -> p.getView().redraw(this.x, this.y));
		
		setPosition(x, y);
	}
	
	public PlayerView getView() {
		return view;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
		subject.onNext(this);
	}
	
	public void move(Direction direction) {
		if(direction == Direction.RIGHT) {
			setPosition(x+2, y);
		}
		else if (direction == Direction.LEFT) {
			if(x > 0 ) {
				setPosition(x-2, y);
			}
		}
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	
	public void setOnTheGround(boolean onTheGround) {
		this.onTheGround = onTheGround;
	}
	
	public boolean isOnTheGround() {
		return onTheGround;
	}
}
