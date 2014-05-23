package rx.crazyeights.controller;

import java.util.ArrayList;

import rx.Observer;
import rx.crazyeights.model.Card;
import rx.crazyeights.model.Game;
import rx.crazyeights.model.Rank;

public class ComputerPlayObserver implements Observer<Long> {
	private Game game;
	
	public ComputerPlayObserver(Game game) {
		this.game = game;
	}
	
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(Long i) {
		ArrayList<Card> hand = game.getComputerPlayer().getHand();
		Card eight = null;
		for(Card c : hand) {
			if(game.validPlay(c)) {
				if(c.getRank() == Rank.EIGHT) {
					eight = c;
				} else {
					game.playCard(game.getComputerPlayer(), c);
					return;
				}
			}
		}
		if(eight != null) {
			game.playCard(game.getComputerPlayer(), eight);
			return;
		}
		
		while(true) {
			//draw card
			Card c = game.drawFromStockPile();
			game.getComputerPlayer().addCard(c);
			
			if(game.validPlay(c)) {
				game.playCard(game.getComputerPlayer(), c);
				return;
			}
		}
	}
	
}
