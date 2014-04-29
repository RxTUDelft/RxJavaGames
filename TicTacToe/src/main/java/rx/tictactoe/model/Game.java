package rx.tictactoe.model;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Game extends Observable<GameState> implements IGameStateManager {

	private final Board board;
	private GameState gameState;
	private Sprite turn;
	private PublishSubject<GameState> subject;

	public Game(Board board, Sprite first) {
		this(board, first, PublishSubject.create());
	}
	
	private Game(Board board, Sprite first, PublishSubject<GameState> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.board = board;

		this.gameState = GameState.STOPPED;
		this.turn = first;
		this.subject = subject;
	}
	
	public Board getBoard() {
		return this.board;
	}

	public void performMove(int x, int y) {
		if (this.gameState == GameState.STARTED) {
			this.board.set(this.turn, x, y);
			this.switchTurns();
		}
	}

	private void switchTurns() {
		if (this.gameState == GameState.STARTED) {
    		if (this.turn == Sprite.X) {
    			this.turn = Sprite.O;
    		}
    		else {
    			assert this.turn == Sprite.O;
    			this.turn = Sprite.X;
    		}
		}
	}

	@Override
	public void startNewGame() {
		this.board.reset();
		this.gameState = GameState.STARTED;
		this.subject.onNext(this.gameState);
	}

	@Override
	public void wonBy(Sprite sprite) {
		switch (sprite) {
			case O:
				this.gameState = GameState.WON_O;
				break;
			case X:
				this.gameState = GameState.WON_X;
				break;
			default:
				throw new IllegalArgumentException("Sprite " + sprite + " is not allowed here!");
		}
		this.subject.onNext(this.gameState);
	}
}
