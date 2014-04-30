package model;

import java.awt.Point;
import java.util.ArrayList;

public class Game {
	private Square[][] squares;
	private Pacman pacman;
	private Happer happer;
	private ArrayList<Block> blocks;
	private ArrayList<Box> boxes;

	public Game() {
		squares = new Square[GameSettings.numberOfHorizontalSquares][GameSettings.numberOfVerticalSquares];
		blocks = new ArrayList<Block>();
		boxes = new ArrayList<Box>();

		createSquares();
		assignNeighbors();

		addPacman();
		addHapper();
		addBoxes();
		addBlocks();
	}

	private void createSquares() {
		for (int x = 0; x < this.squares.length; x++) {
			for (int y = 0; y < this.squares[x].length; y++) {
				this.squares[x][y] = new Square(new Point(x, y));
			}
		}
	}

	public void assignNeighbors() {
		for (int x = 0; x < this.squares.length; x++) {
			for (int y = 0; y < this.squares[x].length; y++) {
				if (y > 0) {
					this.squares[x][y].addNeighbor(Direction.UP,
							this.squares[x][y - 1]);
				}

				if (y < GameSettings.numberOfVerticalSquares - 1) {
					this.squares[x][y].addNeighbor(Direction.DOWN,
							this.squares[x][y + 1]);
				}

				if (x > 0) {
					this.squares[x][y].addNeighbor(Direction.LEFT,
							this.squares[x - 1][y]);
				}

				if (x < GameSettings.numberOfHorizontalSquares - 1) {
					this.squares[x][y].addNeighbor(Direction.RIGHT,
							this.squares[x + 1][y]);
				}
			}
		}
	}

	private void addBoxes() {
		for (int i = 0; i < GameSettings.numberOfBoxes; i++) {
			Square square = getRandomEmptySquare();
			Box box = new Box(square);
			boxes.add(box);
			square.setGameObject(box);
		}
	}
	
	private void addBlocks() 
    {    
        for(int i=0; i<GameSettings.numberOfBlocks; i++)
        {
            Square square = getRandomEmptySquare();
            Block block = new Block(square);
            blocks.add(block);
            square.setGameObject(block);
        }
    }

	public Square getRandomEmptySquare() {
		int x, y;
		do {
			x = (int) (Math.random() * GameSettings.numberOfHorizontalSquares);
			y = (int) (Math.random() * GameSettings.numberOfVerticalSquares);
		} while (squares[x][y].hasGameObject() == true);
		return squares[x][y];
	}

	private void addPacman() {
		Square square = squares[0][0];
		this.pacman = new Pacman(square);
		square.setGameObject(pacman);
	}
	
	private void addHapper() {
		Square square = squares[GameSettings.numberOfHorizontalSquares-1][GameSettings.numberOfVerticalSquares-1];
        happer = new Happer(square);
        square.setGameObject(happer);
	}

	public Pacman getPacman() {
		return pacman;
	}
	
	public Happer getHapper() {
		return happer;
	}

	public ArrayList<Box> getBoxes() {
		return boxes;
	}
	
	public ArrayList<Block> getBlocks() {
		return blocks;
	}
}
