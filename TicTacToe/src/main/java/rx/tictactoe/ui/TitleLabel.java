package rx.tictactoe.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TitleLabel extends Label {

	public TitleLabel(String title) {
		super(title);
		this.setAlignment(Pos.TOP_CENTER);
		this.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	}
}
