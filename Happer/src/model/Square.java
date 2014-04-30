package model;

import java.awt.Point;
import java.util.HashMap;
import java.util.Set;

public class Square {
	private Point position;
	private HashMap<Direction, Square> neighbors;
	private GameObject gameObject;
	private int distance; //distance to Pacman

	public Square(Point position) {
		this.position = position;
		neighbors = new HashMap<Direction, Square>();
		distance = Integer.MAX_VALUE;
	}

	public int getNumberOfNeighbors() {
		return neighbors.size();
	}

	public void distance(int distance) {
		this.distance = distance;
		int nextDistance = distance + 1;

		Set<Direction> set = neighbors.keySet();
		for (Direction dir : set) {
			if (neighbors.get(dir).distance > nextDistance
					&& !neighbors.get(dir).hasGameObject()) {
				neighbors.get(dir).distance(nextDistance);
			}
		}
	}

	public int getDistance() {
		return this.distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void resetDistance() {
		this.distance = Integer.MAX_VALUE;
	}

	public Square getNeighbor(Direction direction) {
		return neighbors.get(direction);
	}

	public HashMap<Direction, Square> getNeighbors() {
		return neighbors;
	}

	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	public GameObject getGameObject() {
		return this.gameObject;
	}

	public boolean hasEmptyNeighborSquare() {
		Set<Direction> set = neighbors.keySet();
		for (Direction r : set) {
			if (!neighbors.get(r).hasGameObject()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasGameObject() {
		if (gameObject != null) {
			return true;
		} else {
			return false;
		}
	}

	public Point getPosition() {
		return position;
	}

	public void addNeighbor(Direction direction, Square neighbor) {
		neighbors.put(direction, neighbor);
	}
}
