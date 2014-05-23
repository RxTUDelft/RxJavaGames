package rx.crazyeights.model;

import java.util.Stack;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import rx.crazyeights.controller.ComputerPlayObserver;
import rx.crazyeights.controller.Observables;
import rx.subjects.PublishSubject;

public class Game {
	private StockPile stockPile;
	private DiscardPile discardPile;
	private Player humanPlayer, computerPlayer;
	private Player turn;
	private Suit overruledSuit;
	private int won, lost;

	public final PublishSubject<String> wonLostObservable = PublishSubject.create();
	public final PublishSubject<Suit> changedSuitObservable = PublishSubject.create();

	public Game() {
		stockPile = new StockPile();
		discardPile = new DiscardPile();
		humanPlayer = new Player("You");
		computerPlayer = new Player("Computer");
		won = 0;
		lost = 0;

		// create 52 playing cards and add them to the stock pile
		for (Suit s : Suit.values()) {
			for (Rank r : Rank.values()) {
				stockPile.addCard(new Card(s, r));
			}
		}

		initializeGame();
	}

	private void initializeGame() {
		turn = humanPlayer;

		//add all cards to stock pile
		stockPile.addCards(discardPile.getAllCards());
		stockPile.addCards(humanPlayer.getAllCards());
		stockPile.addCards(computerPlayer.getAllCards());
		
		// shuffle stock pile
		stockPile.shuffle();

		// give 8 cards to the players
		for (int i = 1; i <= 8; i++) {
			humanPlayer.addCard(stockPile.draw());
			computerPlayer.addCard(stockPile.draw());
		}
		
		// get first card from the stock pile and add it to the discard pile
		discardPile.addCard(stockPile.draw());
	}

	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	public Card drawFromStockPile() {
		Card drawn = stockPile.draw();

		if (stockPile.getSize() == 0) {
			if(discardPile.getSize() > 1) {
				//reuse cards in discard pile
				Stack<Card> discardPileCards = discardPile.getCardsWithoutTop();
				stockPile.addCards(discardPileCards);
				stockPile.shuffle();
			} else {
				//add new card deck
				for (Suit s : Suit.values()) {
					for (Rank r : Rank.values()) {
						stockPile.addCard(new Card(s, r));
					}
				}
				stockPile.shuffle();
			}
		}

		return drawn;
	}

	public Player getHumanPlayer() {
		return humanPlayer;
	}

	public Player getComputerPlayer() {
		return computerPlayer;
	}

	public Player getTurn() {
		return turn;
	}

	public void playCard(Player p, Card c) {
		if (turn.equals(p) && validPlay(c)) {
			overruledSuit = null;
			p.removeCardFromHand(c);
			discardPile.addCard(c);

			if (p.getHand().size() == 0) {
				showEndGameDialog(p);
			} else {
				if (c.getRank() == Rank.EIGHT) {
					showEightDialog(p);
					if(p.equals(computerPlayer)) {
						changedSuitObservable.onNext(overruledSuit);
					}
				} else {
					changedSuitObservable.onNext(null);
				}

				switchTurns();

				if (turn.equals(computerPlayer)) {
					Observables.delay(1000).subscribe(new ComputerPlayObserver(this));
				}
			}
		}
	}

	private void showEndGameDialog(Player p) {
		if (p.equals(humanPlayer)) {
			won++;
			wonLostObservable.onNext("won");
		} else {
			lost++;
			wonLostObservable.onNext("lost");
		}
		Action response = Dialogs.create().title("End game")
				.masthead(p + " won!")
				.message("Do you want to play again?")
				.actions(Dialog.Actions.YES, Dialog.Actions.NO)
				.showConfirm();

		if (response == Dialog.Actions.YES) {
			// restart game
			initializeGame();
		} else {
			System.exit(0);
		}		
	}

	private void showEightDialog(Player p) {
		if(p.equals(humanPlayer)) {
			Suit chosenElement = Dialogs.create()
					.title("Choose a suit")
					.masthead("You played an eight.")
					.message("Choose a suit:")
					.showChoices(Suit.values());

			overruledSuit = chosenElement;
		} else {
			Suit most = p.getSuitWithMostCards();
			overruledSuit  = most;
			Dialogs.create()
		        .title("The suit has been changed")
		        .masthead("The computer played an eight.")
		        .message("The chosen suit is " + most)
		        .showInformation();
		}
	}

	public boolean validPlay(Card c) {
		Card discardPileCard = discardPile.getCardAtTop();

		Suit currentSuit;
		if (overruledSuit != null) {
			currentSuit = overruledSuit;
		} else {
			currentSuit = discardPileCard.getSuit();
		}

		if (c.getSuit() == currentSuit
				|| c.getRank() == discardPileCard.getRank()
				|| c.getRank() == Rank.EIGHT) {
			return true;
		} else {
			return false;
		}
	}

	private void switchTurns() {
		if (turn.equals(humanPlayer)) {
			turn = computerPlayer;
		} else if (turn.equals(computerPlayer)) {
			turn = humanPlayer;
		}
	}

	public int getWon() {
		return won;
	}

	public int getLost() {
		return lost;
	}

}
