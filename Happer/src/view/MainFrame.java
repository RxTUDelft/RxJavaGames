package view;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainFrame extends HBox {
	private StackPane leftPane;
	private StackPane rightPane;

	public MainFrame() {
		leftPane = new StackPane();
		leftPane.setPrefSize(600, 600);
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
