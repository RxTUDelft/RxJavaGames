package rx.happer.model;

import java.util.HashMap;
import java.util.Map.Entry;

public class GameSettings {
	public static int gameSize = 600;
    public static int numberOfHorizontalSquares = 20;
    public static int numberOfVerticalSquares = numberOfHorizontalSquares;
    public static int squareSize = gameSize / numberOfHorizontalSquares;
    public static int numberOfBlocks = 40;
    public static int numberOfBoxes = 60;
    public static int happerSpeed = 750;
    public static boolean grid = true;
    
    //speed number to clock speed
    private static HashMap<Integer, Integer> speedToHapperSpeed;
    static
    {
    	speedToHapperSpeed = new HashMap<Integer, Integer>();
    	speedToHapperSpeed.put(1, 1250);
    	speedToHapperSpeed.put(2, 1000);
    	speedToHapperSpeed.put(3, 750);
    	speedToHapperSpeed.put(4, 500);
    	speedToHapperSpeed.put(5, 250);
    }
    
    public static void setNumberOfBoxes(int boxPercentage){
        numberOfBoxes = numberOfHorizontalSquares * numberOfVerticalSquares / 100 * boxPercentage;
    }
    
    public static void setNumberOfBlocks(int blockPercentage){
        numberOfBlocks = numberOfHorizontalSquares * numberOfVerticalSquares / 100 * blockPercentage;
    }
    
    public static void setHapperSpeed(int speed){
    	happerSpeed = speedToHapperSpeed.get(speed);
    }
    
    public static int getBoxPercentage(){
        int boxPercentage = numberOfBoxes / (numberOfHorizontalSquares * numberOfVerticalSquares / 100);
        return boxPercentage;
    }
    
    public static int getBlockPercentage(){
        int blockPercentage = numberOfBlocks / (numberOfHorizontalSquares * numberOfVerticalSquares / 100);
        return blockPercentage;
    }
    
    public static int getHapperSpeed(){
    	for (Entry<Integer, Integer> entry : speedToHapperSpeed.entrySet()) {
            if (entry.getValue() == happerSpeed) {
                return entry.getKey();
            }
        }
		return 3;
    }
}
