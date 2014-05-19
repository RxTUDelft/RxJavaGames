package rx.crazyeights.controller;

import org.controlsfx.dialog.Dialogs;

import rx.crazyeights.model.Card;
import rx.crazyeights.model.Game;
import rx.crazyeights.view.GameView;

public class GameController {
	
	public GameController(Game game, GameView gameView) {
		
		Observables.mouseClick(gameView.getStockPileView()).subscribe(event -> {
			if(game.getTurn().equals(game.getHumanPlayer())) {
				if(game.getHumanPlayer().getHand().size() >= 37) {
					Dialogs.create()
				        .title("Invalid action")
				        .masthead("This action is invalid.")
				        .message("You are not allowed to have more than 37 playing cards.")
				        .showInformation();
				} else {
					Card drawn = game.drawFromStockPile();
					game.getHumanPlayer().addCard(drawn);
				}
			}
		});
		
		game.wonLostObservable.subscribe(message -> {
			if(message == "won") {
				gameView.getWon().setText(game.getWon() + "");
			} else {
				gameView.getLost().setText(game.getLost() + "");
			}
		});
	}
	
}
