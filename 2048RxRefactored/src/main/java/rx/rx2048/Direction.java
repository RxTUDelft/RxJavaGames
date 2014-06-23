package rx.rx2048;

import javafx.scene.input.KeyCode;
import javafx.scene.input.SwipeEvent;

/**
 * @author bruno.borges@oracle.com
 */
public enum Direction {

    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

    private final int y;
    private final int x;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "Direction{" + "y=" + this.y + ", x=" + this.x + '}' + name();
    }

    public Direction goBack() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        return null;
    }

    public static Direction valueFor(KeyCode keyCode) {
        return valueOf(keyCode.name());
    }

    public static Direction valueFor(SwipeEvent swipeDirection) {
    	return valueOf(swipeDirection.getEventType().getName().replace("SWIPE_", ""));
    }
}
