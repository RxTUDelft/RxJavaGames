package rx.tictactoe.model;

import java.util.Random;

import rx.Observable;
import rx.subjects.PublishSubject;

public class Game extends Observable<GameState> implements IGameStateManager {

	private final Board board;
	private GameState gameState;
	private Sprite turn;
	private final PublishSubject<GameState> subject;

	public Game(Board board) {
		this(board, PublishSubject.create());
		this.startNewGame();
	}
	
	private Game(Board board, PublishSubject<GameState> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.board = board;

		this.turn = this.determineFirst();
		this.gameState = this.turn == Sprite.X ? GameState.TURN_X : GameState.TURN_O;
		this.subject = subject;
	}
	
	private Sprite determineFirst() {
		return new Random().nextBoolean() ? Sprite.X : Sprite.O;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public GameState getGameState() {
		return this.gameState;
	}

	public void performMove(int x, int y) {
		if (this.gameState == GameState.TURN_X
				|| this.gameState == GameState.TURN_O) {
			this.board.set(this.turn, x, y);
		}
		
		if (this.gameState == GameState.TURN_X
				|| this.gameState == GameState.TURN_O) {
			this.switchTurns();
		}
	}

	private void switchTurns() {
		if (this.turn == Sprite.X) {
			this.turn = Sprite.O;
			this.gameState = GameState.TURN_O;
		}
		else {
			assert this.turn == Sprite.O;
			this.turn = Sprite.X;
			this.gameState = GameState.TURN_X;
		}
		this.subject.onNext(this.gameState);
	}

	@Override
	public void startNewGame() {
		this.board.reset();
		switch (this.gameState) {
			case WON_O:
				this.gameState = GameState.TURN_X;
				this.turn = Sprite.X;
				break;
			case WON_X:
				this.gameState = GameState.TURN_O;
				this.turn = Sprite.O;
				break;
			default:
				break;
		}
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
