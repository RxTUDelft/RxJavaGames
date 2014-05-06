package rx.happer.model;

import rx.Observer;
import rx.subjects.BehaviorSubject;
import javafx.scene.image.ImageView;

public class GameObject {
	protected final BehaviorSubject<GameObject> subject = BehaviorSubject.create(this);

	protected ImageView imageView;
	protected Square square;
	
	public GameObject(Square square)
    {
        this.square = square;
    }
	
	public Square getSquare() {
		return square;
	}
	
	public ImageView getImageView() {
		return imageView;
	}
	
	
	public void subscribe(Observer<GameObject> obs) {
		subject.subscribe(obs);
	}	
}
