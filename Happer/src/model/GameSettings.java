package model;

public class GameSettings {
    public static int numberOfHorizontalSquares = 20;
    public static int numberOfVerticalSquares = 20;
    public static int squareSize = 600 / numberOfHorizontalSquares;
    public static int numberOfBlocks = 40;
    public static int numberOfBoxes = 60;
    public static int happerSpeed = 750;
    public static boolean grid = true;
    
    public static void setNumberOfBoxes(int boxPercentage){
        numberOfBoxes = numberOfHorizontalSquares * numberOfVerticalSquares / 100 * boxPercentage;
    }
    
    public static void setNumberOfBlocks(int blockPercentage){
        numberOfBlocks = numberOfHorizontalSquares * numberOfVerticalSquares / 100 * blockPercentage;
    }
    
    public static int getBoxPercentage(){
        int boxPercentage = numberOfBoxes / (numberOfHorizontalSquares * numberOfVerticalSquares / 100);
        return boxPercentage;
    }
    
    public static int getBlockPercentage(){
        int blockPercentage = numberOfBlocks / (numberOfHorizontalSquares * numberOfVerticalSquares / 100);
        return blockPercentage;
    }
}
