package rx.happer.view;

import javafx.scene.control.Label;

public class TextBox extends Label {
	
	public TextBox(String txt, boolean won) {
		super(txt);
		
		if(won) {
			setId("textBoxWon");
		} else {
			setId("textBoxLost");
		}
	}
}
