package rx.rx2048;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import rx.Observable;
import rx.rx2048.rxUtils.Observables;

/**
 * @author bruno.borges@oracle.com
 */
public class Game2048 extends Application {

    private GameManager gameManager;
    private Bounds gameBounds;

    @Override
    public void start(Stage primaryStage) {
        this.gameManager = new GameManager();
        this.gameBounds = this.gameManager.getLayoutBounds();

        StackPane root = new StackPane(this.gameManager);
        root.setPrefSize(this.gameBounds.getWidth(), this.gameBounds.getHeight());
        ChangeListener<Number> resize = (ov, v, v1) -> {
            this.gameManager.setLayoutX((root.getWidth() - this.gameBounds.getWidth()) / 2d);
            this.gameManager.setLayoutY((root.getHeight() - this.gameBounds.getHeight()) / 2d);
        };
        root.widthProperty().addListener(resize);
        root.heightProperty().addListener(resize);

        Scene scene = new Scene(root, 600, 720);
        scene.getStylesheets().add("game.css");
        addKeyHandler(scene);
        addSwipeHandlers(scene);

        if (isARMDevice()) {
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
        }

        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            scene.setCursor(Cursor.NONE);
        }

        primaryStage.setTitle("2048FX");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(this.gameBounds.getWidth());
        primaryStage.setMinHeight(this.gameBounds.getHeight());
        primaryStage.show();
    }

    private boolean isARMDevice() {
        return System.getProperty("os.arch").toUpperCase().contains("ARM");
    }

    /**
     * @author Richard van Heest
     */
    private void addKeyHandler(Scene scene) {
    	Observable<KeyCode> keyHandler = Observables.keyPress(scene).map(KeyEvent::getCode);
    	keyHandler.filter(KeyCode::isArrowKey)
    			.map(Direction::valueFor)
    			.subscribe(this.gameManager::move);
    	keyHandler.filter(KeyCode.R::equals)
				.subscribe(key -> this.gameManager.restoreSession());
    	keyHandler.filter(KeyCode.S::equals)
				.subscribe(key -> this.gameManager.saveSession());
    }

    /**
     * @author Richard van Heest
     */
    private void addSwipeHandlers(Scene scene) {
    	Observables.swipe(scene).map(Direction::valueFor).subscribe(this.gameManager::move);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
