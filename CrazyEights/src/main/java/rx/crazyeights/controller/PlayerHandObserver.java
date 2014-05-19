package rx.crazyeights.controller;

import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import rx.Observer;
import rx.crazyeights.model.Card;
import rx.crazyeights.model.Game;

public class PlayerHandObserver implements Observer<ArrayList<Card>> {
	private Game game;
	private StackPane playerHandPane;

	public PlayerHandObserver(Game game, StackPane playerHandPane) {
		this.game = game;
		this.playerHandPane = playerHandPane;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(ArrayList<Card> hand) {
		playerHandPane.getChildren().clear();
		int numberOfCards = hand.size();
		if(numberOfCards > 0) {
			
			int cardWidth = 90;
			double initialMarginLeft, marginBetweenCards;
			
			if(numberOfCards <= 8) {
				marginBetweenCards = 8;
				initialMarginLeft = 12 + (cardWidth + 8)/2 * (8 - numberOfCards);
			} else {
				initialMarginLeft = 12;
				marginBetweenCards = (776-(numberOfCards*cardWidth)) / (numberOfCards-1.0);
			}
			
			for (int i = 0; i < numberOfCards; i++) {
				Card c = hand.get(i);
				ImageView iv = new ImageView(c.getImage());
				iv.setId("playerCard");
				iv.setFitWidth(cardWidth);
				iv.setPreserveRatio(true);
				iv.setTranslateX(initialMarginLeft + i * (cardWidth + marginBetweenCards));
				playerHandPane.getChildren().add(iv);
	
				Observables.mouseDoubleClick(iv).subscribe(
						event -> game.playCard(game.getHumanPlayer(), c));
			}
		}
	}

}
