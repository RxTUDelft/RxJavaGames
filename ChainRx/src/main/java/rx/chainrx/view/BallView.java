package rx.chainrx.view;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BallView extends Circle {
	
	public BallView() {
		super();
		
		this.setRadius(10);
		
		Random random = new Random();
		
		// max color value to prevent light colored balls that are not visible on the background
		int maxRGBvalue = 240;
		
		int red = random.nextInt(maxRGBvalue-1);
		int green = random.nextInt(maxRGBvalue-1);
		int blue = random.nextInt(maxRGBvalue-1);
		Color color = Color.rgb(red, green, blue);
		
		this.setFill(color);
	}
}
