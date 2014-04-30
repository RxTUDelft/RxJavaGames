package view;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SettingsView extends GridPane {
	Text title;
	Label gridSize, noBoxes, noBlocks, speedOfEnemy;
	Slider gridSlider, boxSlider, blockSlider;
	
	public SettingsView() {
		setHgap(20);
		setVgap(30);
		
		title = new Text("Settings");		
		add(title, 0, 0, 3, 1);
		
		gridSize = new Label("Grid size:");
		add(gridSize,0,1);
		gridSlider = new Slider();
		add(gridSlider,1,1);
		
		noBoxes = new Label("Number of boxes:");
		add(noBoxes,0,2);
		boxSlider = new Slider();
		add(boxSlider,1,2);
		
		noBlocks = new Label("Number of blocks:");
		add(noBlocks,0,3);
		blockSlider = new Slider();
		add(blockSlider,1,3);
		
		speedOfEnemy = new Label("Speed of enemy:");
		add(speedOfEnemy,0,4);
	}
}
