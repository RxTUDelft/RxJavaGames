package model;

public class MovableObject extends GameObject {
	
	public MovableObject(Square square) {
		super(square);
    }
	
	public boolean move(Direction direction) {
        Square neighborSquare = this.square.getNeighbor(direction);
        if (neighborSquare != null) {
            if (!neighborSquare.hasGameObject()) {
                this.square.setGameObject(null);
                neighborSquare.setGameObject(this);
                this.square = neighborSquare;
                subject.onNext(this);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
	
}
