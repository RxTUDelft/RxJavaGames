package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Box extends MovableObject {

	public Box(Square square) {
		super(square);
		
		Image imgBox = new Image("/images/box.png");
		imageView = new ImageView(imgBox);
	}

	
	@Override
    public boolean move(Direction direction) {
        Square neighbor = this.square.getNeighbor(direction);
        if (neighbor != null) {
            if (neighbor.getGameObject() instanceof Box) {
                Box box = (Box) neighbor.getGameObject();
                box.move(direction);
            }
        }
        return super.move(direction);

    }
}
