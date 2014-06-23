package rx.platformer.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import rx.platformer.view.Images;
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
	 * B = box
	 * b = moving object blocker
	 * C = cloud
	 * c = controls
	 * D = door
	 * F = fly
	 * G = grass
	 * K = key
	 * M = moving platform
	 * P = player
	 * p = platform
	 * S = snail
	 * s = slimy
	 * W = water
	 */
	public Level generateLevel(char[][] input) {
		double totalWidth = input[0].length * 70;
		level = new Level(levelView, totalWidth);
		
		int currentX = 0;
		int currentY = 0;
		
		for(int row = 0; row < input.length; row++) {
			currentX = 0;
			for(int column = 0; column < input[0].length; column++) {
				
				switch (input[row][column]) {
				case 'B':
					ImageView box = addImageView(Images.BOX, currentX, currentY, true);
					level.addGround(box);
					level.addSolidBlock(box);
					break;
				case 'b':
					ImageView movingObjectBlocker = addInvisibleBlocker(currentX, currentY);
					level.addMovingObjectBlocker(movingObjectBlocker);
					break;
				case 'C':
					addImageView(Images.CLOUD, currentX, currentY, true);
					break;
				case 'c':
					addText("\u2190\t = move left\n\u2192\t = move right\nz\t = jump", currentX, currentY, true);
					break;
				case 'D':
					ImageView door = addImageView(Images.DOOR, currentX, currentY, true);
					Text txt = addText("You need 3 keys\nto open the door", currentX-35, currentY-70, true);
					level.setDoor(door, txt);
					break;
				case 'F':
					ImageView fly = addImageView(Images.FLY, currentX, currentY, true);
					Enemy flyObj = new Enemy(currentX, fly, 20);
					level.addMovingObject(flyObj);
					level.addEnemy(flyObj);
					level.addGround(fly);
					break;
				case 'G':
					ImageView ground = addImageView(Images.GRASS, currentX, currentY, true);
					level.addGround(ground);
					break;
				case 'K':
					ImageView key = addImageView(Images.KEY, currentX, currentY, true);
					level.addKey(key);
					break;
				case 'M':
					ImageView movingPlatform = addImageView(Images.PLATFORM, currentX, currentY, true);
					level.addGround(movingPlatform);
					MovingObject movingPlatformObj = new MovingObject(currentX, movingPlatform, 30);
					level.addMovingObject(movingPlatformObj);
					level.addMovingPlatform(movingPlatformObj);
					break;
				case 'P':
					Player player = new Player(currentX, currentY);
					level.setPlayer(player);
					levelView.addView(player.getView(), false);
					break;
				case 'p':
					ImageView platform = addImageView(Images.PLATFORM, currentX, currentY, true);
					level.addGround(platform);
					break;
				case 'S':
					ImageView snail = addImageView(Images.SNAIL, currentX, currentY, true);
					Enemy snailObj = new Enemy(currentX, snail, 40);
					level.addMovingObject(snailObj);
					level.addEnemy(snailObj);
					level.addGround(snail);
					break;
				case 's':
					ImageView slimy = addImageView(Images.SLIMY, currentX, currentY, true);
					Enemy slimyObj = new Enemy(currentX, slimy, 50);
					level.addMovingObject(slimyObj);
					level.addEnemy(slimyObj);
					level.addGround(slimy);
					break;
				case 'W':
					addImageView(Images.WATER, currentX, currentY, true);
					break;
				}
				currentX +=70;
			}
			currentY += 70;
		}
		
		return level;
	}
	
	
	private ImageView addImageView(Image img, double x, double y, boolean scrollable) {
		ImageView iv = new ImageView(img);
		iv.setTranslateX(x);
		iv.setTranslateY(y + 70-img.getHeight());
		levelView.addView(iv, scrollable);
		return iv;
	}
	
	private Text addText(String message, double x, double y, boolean scrollable) {
		Text text = new Text(message);
		text.setTranslateX(x + 35 - text.getLayoutBounds().getWidth()/2);
		text.setTranslateY(y);
		text.setStyle("-fx-font-family: Verdana; -fx-font-size: 14pt;");
		levelView.addView(text, scrollable);
		return text;
	}
	
	private ImageView addInvisibleBlocker(double x, double y) {
		ImageView iv = new ImageView();
		iv.setTranslateX(x + 1);
		iv.setTranslateY(y + 1);
		iv.setFitHeight(68);
		iv.setFitWidth(68);
		levelView.addView(iv, true);
		return iv;
	}
	
}
