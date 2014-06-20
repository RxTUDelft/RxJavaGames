package rx.platformer.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import rx.platformer.view.LevelView;

public class LevelGenerator {
	private Level level;
	private LevelView levelView;
	
	public LevelGenerator(LevelView levelView) {
		this.levelView = levelView;
	}
	
	/*
	 * tiles are 70x70 px
	 * 
	 * C = cloud
	 * G = grass
	 * K = key
	 * P = player
	 * S = stone
	 * W = water
	 */
	public Level generateLevel(char[][] input) {
		level = new Level(levelView);
		
		int currentX = 0;
		int currentY = 0;
		
		for(int row = 0; row < input.length; row++) {
			currentX = 0;
			for(int column = 0; column < input[0].length; column++) {
				
				switch (input[row][column]) {
				case 'C':
					addImageView("/cloud1.png", currentX, currentY, 128, true);
					break;
				case 'G':
					ImageView ground = addImageView("/grassMid.png", currentX, currentY, 70, true);
					level.addGround(ground);
					break;
				case 'K':
					ImageView key = addImageView("/keyYellow.png", currentX, currentY, 40, true);
					level.addKey(key);
					break;
				case 'P':
					Player player = new Player(currentX, currentY);
					level.setPlayer(player);
					levelView.addView(player.getView(), false);
					break;
				case 'S':
					ImageView stone = addImageView("/stoneHalf.png", currentX, currentY, 70, true);
					level.addGround(stone);
					break;
				case 'W':
					addImageView("/liquidWaterTop.png", currentX, currentY, 70, true);
					break;
				}
				currentX +=70;
			}
			currentY += 70;
		}
		
		return level;
	}
	
	
	public ImageView addImageView(String imagePath, double x, double y, int size, boolean scrollable) {
		ImageView iv = new ImageView(new Image(imagePath, size, size, true, true));
		iv.setTranslateX(x);
		iv.setTranslateY(y);
		levelView.addView(iv, scrollable);
		return iv;
	}
	
	
}
