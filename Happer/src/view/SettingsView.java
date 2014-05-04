package view;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.GameSettings;
import controller.SettingsController;

public class SettingsView extends GridPane {
	SettingsController settingsController;
	Text title, gridText, boxText, blockText, speedText;
	Label gridSize, noBoxes, noBlocks, speedOfEnemy, drawGrid;
	Slider gridSlider, boxSlider, blockSlider;
	ToggleGroup enemySpeedGroup;
	CheckBox drawGridBox;
	
	public SettingsView() {
		setId("settingsPane");

		settingsController = new SettingsController(this);
		
		title = new Text("Settings");
		title.setId("settingsTitle");
		add(title, 0, 0, 3, 1);
		
		//grid size
		gridSize = new Label("Grid size:");
		add(gridSize,0,1);
		gridSlider = new Slider(10, 30, GameSettings.numberOfHorizontalSquares);
		gridSlider.setBlockIncrement(5);
		gridSlider.setMinorTickCount(0);
		gridSlider.setMajorTickUnit(5);
		gridSlider.setSnapToTicks(true);
		settingsController.addGridSizeListener(gridSlider);
		add(gridSlider,1,1);
		gridText = new Text(GameSettings.numberOfHorizontalSquares + "x" + GameSettings.numberOfVerticalSquares);
		add(gridText,2,1);
		
		//number of boxes
		noBoxes = new Label("Number of boxes:");
		add(noBoxes,0,2);
		boxSlider = new Slider(5, 20, GameSettings.getBoxPercentage());
		boxSlider.setBlockIncrement(1);
		settingsController.addBoxListener(boxSlider);
		add(boxSlider,1,2);
		boxText = new Text(GameSettings.getBoxPercentage() + "%");
		add(boxText,2,2);
		
		//number of blocks
		noBlocks = new Label("Number of blocks:");
		add(noBlocks,0,3);
		blockSlider = new Slider(0, 15, GameSettings.getBlockPercentage());
		blockSlider.setBlockIncrement(1);
		settingsController.addBlockListener(blockSlider);
		add(blockSlider,1,3);
		blockText = new Text(GameSettings.getBlockPercentage() + "%");
		add(blockText,2,3);
		
		speedOfEnemy = new Label("Speed of enemy:");
		add(speedOfEnemy,0,4);
		enemySpeedGroup = new ToggleGroup();
		for(int i=1; i<=5; i++) {
			RadioButton rb = new RadioButton();
			rb.setToggleGroup(enemySpeedGroup);
			rb.setUserData(i);
			rb.setTranslateX((i-1)*30);
			if(i == GameSettings.getHapperSpeed()) {
				rb.setSelected(true);
			}
			add(rb,1,4);
		}
		settingsController.addHapperSpeedListener(enemySpeedGroup);
		
		speedText = new Text(GameSettings.getHapperSpeed() + "");
		add(speedText, 2, 4);
		
		drawGrid = new Label("Draw grid:");
		add(drawGrid,0,5);
		drawGridBox = new CheckBox();
		if(GameSettings.grid) {
			drawGridBox.setSelected(true);
		} else {
			drawGridBox.setSelected(false);
		}
		settingsController.addDrawGridListener(drawGridBox);
		add(drawGridBox,1,5);
	}

	public Text getGridText() {
		return gridText;
	}

	public Text getBoxText() {
		return boxText;
	}

	public Text getBlockText() {
		return blockText;
	}
	
	public Text getSpeedText() {
		return speedText;
	}
}