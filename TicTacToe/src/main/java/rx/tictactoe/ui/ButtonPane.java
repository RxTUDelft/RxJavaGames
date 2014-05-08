package rx.tictactoe.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ButtonPane extends GridPane {

	public ButtonPane(Button... buttons) {
		super();
		this.setVgap(Constants.getUnit() / 6.0);
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setMaxWidth(Double.MAX_VALUE);
			this.add(buttons[i], 0, i);
		}
	}
}
