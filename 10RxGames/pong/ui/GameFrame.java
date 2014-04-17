package ui;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = -626813424537550749L;

	public GameFrame(GamePanel panel, Dimension dim) {
		this.setTitle("Rx Pong");
		this.setSize(dim);
		
		this.add(panel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
