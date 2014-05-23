package rx.crazyeights.controller;

import java.util.ArrayList;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import rx.Observer;
import rx.crazyeights.model.Card;
import rx.crazyeights.model.Game;
import rx.crazyeights.view.GameView;
import rx.functions.Action0;

public class PlayerHandObserver implements Observer<ArrayList<Card>> {
	private Game game;
	private GameView gameView;
	private boolean invalidPlayMessageVisbile;

	public PlayerHandObserver(Game game, GameView gameView) {
		this.game = game;
		this.gameView = gameView;
		this.invalidPlayMessageVisbile = false;
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
		StackPane playerHandPane = gameView.getHumanPlayerHand();
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
	
				Observables.mouseDoubleClick(iv).subscribe(event -> {
					if(game.validPlay(c)) {
						hideInvalidPlayMessage();
						game.playCard(game.getHumanPlayer(), c);
					} else {
						showInvalidPlayMessage();
					}
				});
			}
		}
	}
	
	private void showInvalidPlayMessage() {
		Action0 up = () -> {
			double changedSuitMessageY = gameView.getChangedSuitMessage().getTranslateY();
			TranslateTransition upTransition = new TranslateTransition(Duration.millis(300), gameView.getInvalidPlayMessage());
			upTransition.setToY(changedSuitMessageY - 65);
			upTransition.play();
		};
		
		if(invalidPlayMessageVisbile) {
			hideInvalidPlayMessage().setOnFinished(event -> up.call());
		} else {
			up.call();
		}
		
		invalidPlayMessageVisbile = true;
	}
	
	private TranslateTransition hideInvalidPlayMessage() {
		TranslateTransition down = new TranslateTransition(Duration.millis(200), gameView.getInvalidPlayMessage());
		if(invalidPlayMessageVisbile) {
			down.setToY(65);
			down.play();
		}
		invalidPlayMessageVisbile = false;
		return down;
	}

}
