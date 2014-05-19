package rx.crazyeights.model;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class Player extends Observable<ArrayList<Card>> {
	private BehaviorSubject<ArrayList<Card>> subject;
	String name;
	private ArrayList<Card> hand;
	
	public Player(String name) {
		this(name, BehaviorSubject.create(new ArrayList<Card>()));
	}
	
	private Player(String name, BehaviorSubject<ArrayList<Card>> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.subject = subject;
		this.name = name;
		this.hand = new ArrayList<>();
	}
	
	public void removeCardFromHand(Card c) {
		hand.remove(c);
		subject.onNext(hand);
	}
	
	public void addCard(Card c) {
		hand.add(c);
		subject.onNext(hand);
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	public ArrayList<Card> getAllCards() {
		ArrayList<Card> list = new ArrayList<Card>(hand);
		hand.clear();
		return list;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public Suit getSuitWithMostCards() {
		int numberOfSpades = 0, numberOfHearts = 0, numberOfDiamonds = 0, numberOfClubs = 0;
		
		for(Card card : hand) {
			switch (card.getSuit()) {
			case SPADES:
				numberOfSpades++;
				break;
			case HEARTS:
				numberOfHearts++;
				break;
			case DIAMONDS:
				numberOfDiamonds++;
				break;
			case CLUBS:
				numberOfClubs++;
				break;
			}
		}
		
		int mostCards = numberOfClubs;
		Suit mostSuit = Suit.CLUBS;
		if(numberOfDiamonds > mostCards) {
			mostCards = numberOfDiamonds;
			mostSuit = Suit.DIAMONDS;
		}
		if(numberOfHearts > mostCards) {
			mostCards = numberOfHearts;
			mostSuit = Suit.HEARTS;
		}
		if(numberOfSpades > mostCards) {
			mostCards = numberOfSpades;
			mostSuit = Suit.SPADES;
		}
		
		return mostSuit;
	}
}