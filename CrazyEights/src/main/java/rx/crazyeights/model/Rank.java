package rx.crazyeights.model;

public enum Rank {
	TWO("02"),
	THREE("03"),
	FOUR("04"),
	FIVE("05"),
	SIX("06"),
	SEVEN("07"),
	EIGHT("08"),
	NINE("09"),
	TEN("10"),
	JACK("11"),
	QUEEN("12"),
	KING("13"),
	ACE("14");
	
	
	private String pathString;

	private Rank(String pathString) {
		this.pathString = pathString;
	}
	
	public String toPathString() {
		return this.pathString;
	}
}