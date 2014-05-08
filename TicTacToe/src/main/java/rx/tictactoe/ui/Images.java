package rx.tictactoe.ui;

import javafx.scene.image.Image;

public enum Images {

	CROSS(new Image("Cross.png")),
	OVAL(new Image("Oval.png")),
	EMPTY(null);

	private Image im;

	private Images(Image im) {
		this.im = im;
	}

	public Image getImage() {
		return this.im;
	}
}
