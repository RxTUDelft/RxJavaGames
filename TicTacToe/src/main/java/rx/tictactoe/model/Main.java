package rx.tictactoe.model;

public class Main {

	public static void main(String[] args) {
		Tile tile1 = new Tile(2, 1);
		Tile tile2 = new Tile(2, 2);
		tile1.subscribe(System.out::println);
		tile2.subscribe(System.out::println);
		
		System.out.println(tile1);
		System.out.println(tile2);
		tile1.setSprite(Sprite.X);
		tile2.setSprite(Sprite.O);
	}
}
