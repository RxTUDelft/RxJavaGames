package rx.tictactoe.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import rx.tictactoe.model.Sprite;

public class BorderedTitledPane extends GridPane {

	private final StackPane contentPane = new StackPane();
	private Label winningLabel = new Label("X has won!!!");

	public BorderedTitledPane(String titleString, Node content) {
		Label titleLabel = new Label(" " + titleString + " ");
		titleLabel.setTranslateY(10.0);
		titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);
		
		this.winningLabel.setTranslateY(-50.0);
		this.winningLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		StackPane.setAlignment(this.winningLabel, Pos.TOP_CENTER);

		this.contentPane.setStyle("-fx-content-display: top;\n"
				+ "-fx-border-insets: 20 15 15 15;\n"
				+ "-fx-border-color: black;\n"
				+ "-fx-border-width: 2;");
		this.setContent(content);
		
		this.setPadding(new Insets(26, 10, 10, 10));
		this.add(titleLabel, 1, 0);
		this.add(this.winningLabel, 1, 0);
		this.add(this.contentPane, 0, 1, 3, 1);
	}
	
	public void setContent(Node content) {
		this.contentPane.getChildren().add(content);
	}
	
	public void setWinningText(Sprite sprite) {
		this.winningLabel.setText(sprite.name() + " has won!!!");
	}
	
	public void setWinningTextVisible(boolean flag) {
		this.winningLabel.setVisible(flag);
	}
}
