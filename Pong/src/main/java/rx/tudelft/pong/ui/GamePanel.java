package rx.tudelft.pong.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.model.ball.Ball;
import rx.tudelft.pong.model.paddle.Paddle;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = -7004429743291206813L;
	private final GameState state;

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
		Rectangle clip = graphics.getClipBounds();
		int x = clip.x;
		int y = clip.y;
		int w = clip.width;
		int h = clip.height;

		graphics.setBackground(Color.BLACK);
		graphics.clearRect(x, y, w, h);

		graphics.setColor(Color.WHITE);

		Integer paddleWidth = Double.valueOf(Paddle.paddleWidth * w).intValue();
		Integer paddleHeight = Double.valueOf(Paddle.paddleHeigt * h).intValue();
		Integer ballDiameter = Double.valueOf(Ball.ballDiameter * w).intValue();

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
		graphics.fillOval(Double.valueOf(this.state.getBall().getPositionX() * w).intValue()
				- ballDiameter / 2,
				Double.valueOf(this.state.getBall().getPositionY() * h).intValue()
						- ballDiameter / 2,
				ballDiameter,
				ballDiameter);
	}
}
