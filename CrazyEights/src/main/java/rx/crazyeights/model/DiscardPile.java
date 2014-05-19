package rx.crazyeights.model;

import java.util.Stack;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class DiscardPile extends Observable<DiscardPile> {
	private BehaviorSubject<DiscardPile> subject;
	private Stack<Card> cards;
	
	public DiscardPile() {
		this(BehaviorSubject.create((DiscardPile) null));
	}
	
	private DiscardPile(BehaviorSubject<DiscardPile> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.subject = subject;
		cards = new Stack<Card>();
	}
	
	public void addCard(Card card) {
		cards.add(card);
		subject.onNext(this);
	}
	
	public Card getCardAtTop() {
		return cards.peek();
	}
	
	public Stack<Card> getCardsWithoutTop() {
		Card top = cards.pop();
		Stack<Card> cardsWithoutTop = cards;
		cards = new Stack<Card>();
		cards.add(top);
		return cardsWithoutTop;
	}
	
	public Stack<Card> getAllCards() {
		Stack<Card> stack = new Stack<Card>();
		stack.addAll(cards);
		cards.clear();
		return stack;
	}
	
	public int getSize() {
		return cards.size();
	}
}
