package rx.rx2048;

/**
 * @author bruno.borges@oracle.com
 */
public class Location {

    private final int x;
    private final int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location offset(Direction direction) {
        return new Location(this.x + direction.getX(), this.y + direction.getY());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + this.x + ", y=" + this.y + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    public double getLayoutY(int CELL_SIZE) {
        if (this.y == 0) {
            return CELL_SIZE / 2;
        }
        return (this.y * CELL_SIZE) + CELL_SIZE / 2;
    }

    public double getLayoutX(int CELL_SIZE) {
        if (this.x == 0) {
            return CELL_SIZE / 2;
        }
        return (this.x * CELL_SIZE) + CELL_SIZE / 2;
    }

    public boolean isValidFor(int gridSize) {
        return this.x >= 0 && this.x < gridSize && this.y >= 0 && this.y < gridSize;
    }

    public boolean validFor(Direction direction, int gridSize) {
        switch (direction) {
            case UP:
                return this.x >= 0;
            case RIGHT:
                return this.y < gridSize;
            case DOWN:
                return this.x < gridSize;
            case LEFT:
                return this.y >= 0;
        }
        return false;
    }

}
