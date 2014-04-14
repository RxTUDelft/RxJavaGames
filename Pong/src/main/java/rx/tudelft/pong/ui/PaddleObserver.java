package rx.tudelft.pong.ui;

import rx.Observer;
import rx.tudelft.pong.model.paddle.Paddle;

public class PaddleObserver implements Observer<Paddle> {
	
	private final GamePanel gamePanel;
	
	public PaddleObserver(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void onCompleted() {
		
	}

	@Override
	public void onError(Throwable e) {
		
	}

	@Override
	public void onNext(Paddle t) {
		this.gamePanel.repaint();
	}
}
