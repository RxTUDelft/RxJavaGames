package rx.crazyeights.model;

public enum Suit {
	DIAMONDS("1"),
	CLUBS("2"),
	HEARTS("3"),
	SPADES("4");
	
	
	private String pathString;

	private Suit(String pathString) {
		this.pathString = pathString;
	}
	
	public String toPathString() {
		return this.pathString;
	}
}
