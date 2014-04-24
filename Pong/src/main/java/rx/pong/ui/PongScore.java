package rx.pong.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PongScore extends Text {

	public PongScore() {
		super();
		this.setFont(Font.font("Courier New", FontWeight.BOLD, 50));
		this.setFill(Color.WHITE);
		this.setText("0");
		this.setTextAlignment(TextAlignment.CENTER);
	}
}
