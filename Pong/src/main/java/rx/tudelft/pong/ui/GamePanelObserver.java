package rx.tudelft.pong.ui;

import java.util.Observable;
import java.util.Observer;

public class GamePanelObserver implements Observer {

	private final GamePanel gamePanel;
	
	public GamePanelObserver(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.gamePanel.repaint();
	}
}
