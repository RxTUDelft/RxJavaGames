package rx.crazyeights.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class StockPile {
	private Stack<Card> cards;
	
	public StockPile() {
		cards = new Stack<Card>();
	}

	public void addCard(Card card) {
		cards.add(card);
	}
	
	public void addCards(Collection<? extends Card> c) {
		cards.addAll(c);
	}
	
	public Card draw() {
		return cards.pop();
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public int getSize() {
		return cards.size();
	}
}
