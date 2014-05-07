package rx.happer.model;

import java.util.ArrayList;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import rx.Observer;
import rx.subjects.PublishSubject;

public class Happer extends MovableObject {
	private final PublishSubject<Boolean> endGameObservable;

	private final Image happerRight = new Image("happer_right.png");
	private final Image happerLeft = new Image("happer_left.png");
	private final Image happerUp = new Image("happer_up.png");
	private final Image happerDown = new Image("happer_down.png");

	public Happer(Square square) {
		super(square);

		endGameObservable = PublishSubject.create();
		imageView = new ImageView(happerLeft);
	}

	public boolean move() {
		Direction direction = getDirection();

		switch (direction) {
		case LEFT:
			imageView.setImage(happerLeft);
			break;
		case RIGHT:
			imageView.setImage(happerRight);
			break;
		case UP:
			imageView.setImage(happerUp);
			break;
		case DOWN:
			imageView.setImage(happerDown);
			break;
		}

		if (!this.square.hasEmptyNeighborSquare()) {
			// win
			endGameObservable.onNext(true);
			return false;
		}
		
		Square neighborSquare = square.getNeighbor(direction);
		if (neighborSquare.getGameObject() instanceof Pacman) {
			// lose
			neighborSquare.setGameObject(this);
			this.square = neighborSquare;
			subject.onNext(this);
			endGameObservable.onNext(false);
			return true;
		}
		return super.move(direction);
	}

	// get shortest path to pacman
	private Direction getDirection() {
		Direction direction = getRandomDirection();
		int smallestNumber = Integer.MAX_VALUE;
		Set<Direction> set = square.getNeighbors().keySet();
		for (Direction d : set) {
			if (square.getNeighbors().get(d).getDistance() < smallestNumber) {
				smallestNumber = square.getNeighbors().get(d).getDistance();
				direction = d;
			}
		}
		return direction;
	}

	private Direction getRandomDirection() {
		Direction direction = Direction.UP;

		ArrayList<Direction> directions = new ArrayList<Direction>();

		Set<Direction> set = square.getNeighbors().keySet();
		for (Direction d : set) {
			if (!square.getNeighbor(d).hasGameObject()) {
				directions.add(d);
			}
		}

		if (directions.size() > 0) {
			int randomNumber = (int) (Math.random() * directions.size());
			direction = directions.get(randomNumber);
		}

		return direction;
	}

	public void subscribeOnEndGame(Observer<Boolean> obs) {
		endGameObservable.subscribe(obs);
	}
}
