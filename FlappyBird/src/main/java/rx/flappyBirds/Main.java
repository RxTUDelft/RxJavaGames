package rx.flappyBirds;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) {
		int screenWidth = 691;
		int screenHeight = 921;
		
		StackPane root = new StackPane();
		root.setAlignment(Pos.BOTTOM_LEFT);
		Scene scene = new Scene(root, screenWidth, screenHeight);

		// Background
		Image bgImg = new Image("background.png");
		ImageView background = new ImageView(bgImg);
		root.getChildren().add(background);
		background.setTranslateY(-115);

		// Bottom
		Image bottomImg = new Image("ground.png");
		double grassWidth = bottomImg.getWidth();
		double grassHeight = bottomImg.getHeight();
		int numberOfTiles = Double.valueOf(Math.ceil(screenWidth / grassWidth)).intValue();

		// Place tiles on bottom, spaced grassWidth apart
		List<ImageView> grass = new ArrayList<>();
		for (int i = 0; i < numberOfTiles; i++) {
			ImageView tile = new ImageView(bottomImg);
			root.getChildren().add(tile);
			tile.setTranslateX(i * grassWidth);
			grass.add(tile);
		}
		
		stage.setTitle("Flappy Bird");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch();
	}
}
