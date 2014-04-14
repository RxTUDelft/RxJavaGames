package rx.tudelft.pong.ui;

import rx.Observer;
import rx.tudelft.pong.model.ball.Ball;

public class BallObserver implements Observer<Ball> {

	private final GamePanel gamePanel;
	
	public BallObserver(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void onCompleted() {
		
	}

	@Override
	public void onError(Throwable e) {
		
	}

	@Override
	public void onNext(Ball t) {
		this.gamePanel.repaint();
	}
}
