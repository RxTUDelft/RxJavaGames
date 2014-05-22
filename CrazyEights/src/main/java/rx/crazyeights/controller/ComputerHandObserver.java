package rx.crazyeights.controller;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import rx.Observer;
import rx.crazyeights.model.Card;
import rx.crazyeights.model.Game;
import rx.crazyeights.view.GameView;

public class ComputerHandObserver implements Observer<ArrayList<Card>> {
	private Game game;
	private GameView gameView;
	private boolean showComputerCards;

	public ComputerHandObserver(Game game, GameView gameView) {
		this.game = game;
		this.gameView = gameView;
		this.showComputerCards = false;
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
		StackPane computerHandPane = gameView.getComputerPlayerHand();
		computerHandPane.getChildren().clear();
		int numberOfCards = hand.size();

		int cardWidth = 90;
		double widthPerCard;
		if(numberOfCards <= 18) {
			widthPerCard = 40;
		} else {
			widthPerCard = (770-cardWidth) / (numberOfCards-1.0);
		}
		double totalHandWidth = widthPerCard * (numberOfCards-1) + cardWidth;
		double initialMarginLeft = (800-totalHandWidth)/2;

		for (int i = 0; i < numberOfCards; i++) {
			Image img;
			if(showComputerCards) {
				img = game.getComputerPlayer().getHand().get(i).getImage();
			} else {
				img = new Image("/card_back.png");
			}
			
			ImageView iv = new ImageView(img);
			iv.setFitWidth(cardWidth);
			iv.setPreserveRatio(true);
			iv.setTranslateX(initialMarginLeft + i * widthPerCard);
			computerHandPane.getChildren().add(iv);
		}
	}

}
