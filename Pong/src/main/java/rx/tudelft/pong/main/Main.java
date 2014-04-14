package rx.tudelft.pong.main;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import rx.observables.SwingObservable;
import rx.tudelft.pong.controller.KeyController;
import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.ui.BallObserver;
import rx.tudelft.pong.ui.GameFrame;
import rx.tudelft.pong.ui.GamePanel;
import rx.tudelft.pong.ui.PaddleObserver;

public final class Main {

	private Main() {
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
    		//Model
    		GameState state = new GameState();
    		
    		//ui
    		Dimension dim = new Dimension(800, 600);
    		GamePanel panel = new GamePanel(state, dim);
    		new GameFrame(panel, dim);
    		
    		//ui observer
    		PaddleObserver paddleObserver = new PaddleObserver(panel);
    		BallObserver ballObserver = new BallObserver(panel);
    		state.getPlayer1().observable.subscribe(paddleObserver);
    		state.getPlayer2().observable.subscribe(paddleObserver);
    		state.getBall().observable.subscribe(ballObserver);
    		
    		//controller
    		new KeyController(state, SwingObservable.fromPressedKeys(panel).publish());
		});
	}
}
