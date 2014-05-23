package rx.crazyeights.model;

import javafx.scene.image.Image;

public class Card {
	private Image img;
	private Suit suit;
	private Rank rank;
	
	public Card(Suit suit, Rank rank) {
		img = new Image(getCardImagePath(suit, rank));
		this.suit = suit;
		this.rank = rank;
	}
	
	public Image getImage() {
		return img;
	}
	
	private String getCardImagePath(Suit suit, Rank rank) {
		return "card"+suit.toPathString()+rank.toPathString()+".png";
	}
	
	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return suit+" "+rank;
	}
	
	
	
}
