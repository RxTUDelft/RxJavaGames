package rx.crazyeights.model;

public enum Suit {
	DIAMONDS("1", "(\u2666)"),
	CLUBS("2", "(\u2663)"),
	HEARTS("3", "(\u2665)"),
	SPADES("4", "(\u2660)");
	
	
	private String pathString;
	private String icon;

	private Suit(String pathString, String icon) {
		this.pathString = pathString;
		this.icon = icon;
	}
	
	public String toPathString() {
		return this.pathString;
	}

	@Override
	public String toString() {
		return super.toString() + " " + icon;
	}
	
}
