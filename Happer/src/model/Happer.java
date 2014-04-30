package model;

import java.util.ArrayList;
import java.util.Set;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Happer extends MovableObject {

	private final Image happerRight = new Image(getClass().getResourceAsStream("/images/happer_right.png"));
	private final Image happerLeft = new Image(getClass().getResourceAsStream("/images/happer_left.png"));
	private final Image happerUp = new Image(getClass().getResourceAsStream("/images/happer_up.png"));
	private final Image happerDown = new Image(getClass().getResourceAsStream("/images/happer_down.png")); 
	
	
	public Happer(Square square) {
		super(square);

		imageView = new ImageView(happerLeft);
	}
	
	public boolean move() {
        Direction direction = getDirection();
        if(!this.square.hasEmptyNeighborSquare()){
            //win
        	return false;
        }
        if(square.getNeighbor(direction).getGameObject() instanceof Pacman)
        {
            //lose
            return false;
        }
     return super.move(direction);
    }

	
	//get shortest path to pacman
	private Direction getDirection() {
        Direction direction = getRandomDirection();
        int smallestNumber = Integer.MAX_VALUE;
        Set<Direction> set = square.getNeighbors().keySet();
        for(Direction d : set){
            if(square.getNeighbors().get(d).getDistance() < smallestNumber){
                smallestNumber = square.getNeighbors().get(d).getDistance();
                direction = d;
            }
        }
        return direction;
    }
	
	
	private Direction getRandomDirection() {
        Direction direction = Direction.UP;
        
        ArrayList<Direction> directions = new ArrayList<Direction>();
                
        Set<Direction> set = square.getNeighbors().keySet();
        for(Direction d : set){
            if(!square.getNeighbor(d).hasGameObject()){
                directions.add(d);
            }
        }
        
        if(directions.size() > 0)
        {
            int randomNumber = (int) (Math.random() * directions.size());
            direction = directions.get(randomNumber);
        }
        
        return direction;
        
    }
}
