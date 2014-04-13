package rx.tudelft.pong.main;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import rx.observables.SwingObservable;
import rx.tudelft.pong.controller.KeyController;
import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.ui.GameFrame;
import rx.tudelft.pong.ui.GamePanel;
import rx.tudelft.pong.ui.GamePanelObserver;

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
    		GamePanelObserver panelObserver = new GamePanelObserver(panel);
    		state.getPlayer1().addObserver(panelObserver);
    		state.getPlayer2().addObserver(panelObserver);
    		state.getBall().addObserver(panelObserver);
    		
    		//controller
    		new KeyController(state, SwingObservable.fromPressedKeys(panel).publish());
		});
	}
}
