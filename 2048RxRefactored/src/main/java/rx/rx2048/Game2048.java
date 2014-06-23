package rx.rx2048;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

    private void addKeyHandler(Scene scene) {
        scene.setOnKeyPressed(ke -> {
            KeyCode keyCode = ke.getCode();
            if (keyCode.equals(KeyCode.S)) {
                this.gameManager.saveSession();
                return;
            }
            if (keyCode.equals(KeyCode.R)) {
                this.gameManager.restoreSession();
                return;
            }
            if (keyCode.isArrowKey() == false) {
                return;
            }
            Direction direction = Direction.valueFor(keyCode);
            this.gameManager.move(direction);
        });
    }

    private void addSwipeHandlers(Scene scene) {
        scene.setOnSwipeUp(e -> this.gameManager.move(Direction.UP));
        scene.setOnSwipeRight(e -> this.gameManager.move(Direction.RIGHT));
        scene.setOnSwipeLeft(e -> this.gameManager.move(Direction.LEFT));
        scene.setOnSwipeDown(e -> this.gameManager.move(Direction.DOWN));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
