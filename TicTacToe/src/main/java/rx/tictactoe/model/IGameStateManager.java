package rx.tictactoe.model;

public interface IGameStateManager {
	
	void startNewGame();
	
	void wonBy(Sprite sprite);
	
	void draw();
}
