package controller;

import main.Main;
import model.Direction;
import model.Game;

public class GameController {
	Game game;

	public GameController(Game game) {
		this.game = game;

		// pacman key observables
		KeyObservables.rightArrowKey(Main.scene).subscribe(event -> {
			game.getPacman().move(Direction.RIGHT);
		});

		KeyObservables.leftArrowKey(Main.scene).subscribe(event -> {
			game.getPacman().move(Direction.LEFT);
		});

		KeyObservables.upArrowKey(Main.scene).subscribe(event -> {
			game.getPacman().move(Direction.UP);
		});

		KeyObservables.downArrowKey(Main.scene).subscribe(event -> {
			game.getPacman().move(Direction.DOWN);
		});
	}
}
