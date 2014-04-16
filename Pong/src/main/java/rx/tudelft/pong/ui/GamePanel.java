package rx.tudelft.pong.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import rx.Observer;
import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.model.ball.Ball;

public class GamePanel extends JPanel implements Observer<GameState> {

	private static final long serialVersionUID = -7004429743291206813L;
	private GameState state;
	
	public static final double paddleWidth = 0.02;
	public static final double paddleHeigth = 0.1;

	public GamePanel(GameState state, Dimension dim) {
		super();
		this.state = state;

		this.setPreferredSize(dim);
		this.setFocusable(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		super.paintComponent(graphics);
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		
		Rectangle clip = graphics.getClipBounds();
		int x = clip.x;
		int y = clip.y;
		int w = clip.width;
		int h = clip.height;
		
		graphics.setBackground(Color.BLACK);
		graphics.clearRect(x, y, w, h);

		graphics.setColor(Color.WHITE);

		Integer paddleWidth = Double.valueOf(GamePanel.paddleWidth * w).intValue();
		Integer paddleHeight = Double.valueOf(GamePanel.paddleHeigth * h).intValue();
		Integer ballDiameter = Double.valueOf(Ball.ballDiameter * w).intValue();
		
		//TODO: paddleUI en ballUI in separate classes.
//		Rectangle2D.Double s = new Rectangle2D.Double(x, y, w/3, w/3);
//		graphics.draw(s);
//		graphics.fill(s);

		//left paddle
		graphics.fillRect(x,
				y + Double.valueOf(this.state.getPlayer1().getPosition() * h).intValue()
						- paddleHeight / 2,
				paddleWidth,
				paddleHeight);
		
		//right paddle
		graphics.fillRect(x + w - paddleWidth,
				y + Double.valueOf(this.state.getPlayer2().getPosition() * h).intValue()
						- paddleHeight / 2,
				paddleWidth,
				paddleHeight);
		
		//ball
		graphics.fillOval(x + Double.valueOf(this.state.getBall().getPositionX() * w).intValue() - ballDiameter / 2,
				y + Double.valueOf(this.state.getBall().getPositionY() * h).intValue() - ballDiameter / 2,
				ballDiameter,
				ballDiameter);
	}

	@Override
	public void onCompleted() {
		//TODO
	}

	@Override
	public void onError(Throwable e) {
		//TODO
	}

	@Override
	public void onNext(GameState t) {
		if (!this.state.equals(t)) {
			this.state = t;
			this.repaint();
		}
	}
}
