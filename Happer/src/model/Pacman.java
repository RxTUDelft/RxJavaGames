package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pacman extends MovableObject {
	private final Image pacmanRight = new Image(getClass().getResourceAsStream("/images/pacman_right.png"));
	private final Image pacmanLeft = new Image(getClass().getResourceAsStream("/images/pacman_left.png"));
	private final Image pacmanUp = new Image(getClass().getResourceAsStream("/images/pacman_up.png"));
	private final Image pacmanDown = new Image(getClass().getResourceAsStream("/images/pacman_down.png")); 
	
	public Pacman(Square square) {
		super(square);
		
		imageView = new ImageView(pacmanRight);
	}


	@Override
	public boolean move(Direction direction) {
		switch(direction) {
			case LEFT:
	            imageView.setImage(pacmanLeft);
	            break;
	        case RIGHT:
	            imageView.setImage(pacmanRight);
	            break;
	        case UP:
	            imageView.setImage(pacmanUp);
	            break;
	        case DOWN:
	            imageView.setImage(pacmanDown);
	            break;
		}
		
		Square neighborSquare = this.square.getNeighbor(direction);
		if (neighborSquare != null) {
			if (neighborSquare.getGameObject() instanceof Box) {
				Box box = (Box) neighborSquare.getGameObject();
				box.move(direction);
			}
		}
		boolean successfull = super.move(direction);

		if (successfull) {
			this.square.distance(0);
			subject.onNext(this);
		}
		return successfull;
	}

}
