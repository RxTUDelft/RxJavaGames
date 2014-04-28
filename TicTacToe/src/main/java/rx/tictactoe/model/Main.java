package rx.tictactoe.model;

public class Main {

	public static void main(String[] args) {
//		Tile tile1 = new Tile(2, 1);
//		Tile tile2 = new Tile(2, 2);
//		tile1.subscribe(System.out::println);
//		tile2.subscribe(System.out::println);
//		
//		System.out.println(tile1);
//		System.out.println(tile2);
//		tile1.setSprite(Sprite.X);
//		tile2.setSprite(Sprite.O);
		
		Game game = new Game(3);
		game.getBoard().set(Sprite.X, 1, 1);
		game.getBoard().set(Sprite.O, 0, 2);
		game.getBoard().set(Sprite.X, 0, 1);
		game.getBoard().set(Sprite.O, 0, 0);
		game.getBoard().set(Sprite.X, 2, 1);
	}
}
