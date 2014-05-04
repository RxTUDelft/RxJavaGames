package model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block extends GameObject {

	public Block(Square square) {
		super(square);
		
		Image imgBlock = new Image("/images/block.png");
		imageView = new ImageView(imgBlock);
	}

}
