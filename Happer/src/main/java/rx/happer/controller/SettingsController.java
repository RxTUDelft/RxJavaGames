package rx.happer.controller;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import rx.happer.model.GameSettings;
import rx.happer.view.SettingsView;

public class SettingsController {

	SettingsView settingsView;
	private int numberOfHorizontalSquares;
	private int numberOfVerticalSquares;
	private int blockPercentage;
	private int boxPercentage;
	private int happerSpeed;
	private boolean grid;
	
	public SettingsController(SettingsView settingsView) {
		this.settingsView = settingsView;
		
		numberOfHorizontalSquares = GameSettings.numberOfHorizontalSquares;
		numberOfVerticalSquares = GameSettings.numberOfVerticalSquares;
		blockPercentage = GameSettings.getBlockPercentage();
		boxPercentage = GameSettings.getBoxPercentage();
		happerSpeed = GameSettings.getHapperSpeed();
		grid = GameSettings.grid;
	}

	public void addGridSizeListener(Slider slider) {
		Observables.sliderChange(slider).subscribe(newValue -> {
			int waarde = Math.round(newValue.intValue() / 5 * 5);
			numberOfHorizontalSquares = waarde;
			numberOfVerticalSquares = waarde;
			changeSettings();
			settingsView.getGridText().setText(GameSettings.numberOfHorizontalSquares + "x" + GameSettings.numberOfVerticalSquares);
		});
	}
	
	public void addBlockListener(Slider slider) {
		Observables.sliderChange(slider).subscribe(newValue -> {
			int waarde = Math.round(newValue.intValue());
			blockPercentage = waarde;
			changeSettings();
			settingsView.getBlockText().setText(GameSettings.getBlockPercentage() + "%");
		});
	}
	
	public void addBoxListener(Slider slider) {
		Observables.sliderChange(slider).subscribe(newValue -> {
			int waarde = Math.round(newValue.intValue());
			boxPercentage = waarde;
			changeSettings();
			settingsView.getBoxText().setText(GameSettings.getBoxPercentage() + "%");
		});
	}
	
	public void addHapperSpeedListener(ToggleGroup tg) {
		Observables.toggleChange(tg).subscribe(newValue -> {
			happerSpeed = (int) newValue.getUserData();
			changeSettings();
			settingsView.getSpeedText().setText(GameSettings.getHapperSpeed() + "");
		});
	}
	
	public void addDrawGridListener(CheckBox cb) {
		Observables.checkBoxChange(cb).subscribe(newValue -> {
			grid = newValue;
			changeSettings();
		});
	}
	
	private void changeSettings() {
        GameSettings.numberOfHorizontalSquares = numberOfHorizontalSquares;
        GameSettings.numberOfVerticalSquares = numberOfVerticalSquares;
        GameSettings.setNumberOfBlocks(blockPercentage);
        GameSettings.setNumberOfBoxes(boxPercentage);
        GameSettings.setHapperSpeed(happerSpeed);
        GameSettings.grid = grid;
        GameSettings.squareSize = GameSettings.gameSize/GameSettings.numberOfHorizontalSquares;
        
    }

}
