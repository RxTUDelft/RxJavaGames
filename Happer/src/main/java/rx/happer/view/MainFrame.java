package rx.happer.view;

import rx.happer.model.GameSettings;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainFrame extends HBox {
	private StackPane leftPane;
	private StackPane rightPane;

	public MainFrame() {
		getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

		leftPane = new StackPane();
		leftPane.setPrefSize(GameSettings.gameSize, GameSettings.gameSize);
		setLeftPane(new StartScreen());
		
		rightPane = new StackPane();
		rightPane.getChildren().add(new Menu(this));
		
		this.getChildren().add(leftPane);
		this.getChildren().add(rightPane);
	}
	
	public void setLeftPane(Pane p)
	{
		leftPane.getChildren().clear();
		leftPane.getChildren().add(p);
	}
}
