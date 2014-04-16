package rx.tudelft.pong.main;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

import rx.observables.SwingObservable;
import rx.tudelft.pong.controller.KeyController;
import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.ui.GameFrame;
import rx.tudelft.pong.ui.GamePanel;

public final class Main {

	private Main() {
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
    		//Model
    		GameState initState = new GameState();
    		
    		//ui
    		Dimension dim = new Dimension(800, 600);
    		GamePanel panel = new GamePanel(initState, dim);
    		new GameFrame(panel, dim);
    		
    		//controller
    		KeyController kc = new KeyController(initState, SwingObservable.fromPressedKeys(panel).publish());
    		kc.subscribe(panel);
		});
	}
}
