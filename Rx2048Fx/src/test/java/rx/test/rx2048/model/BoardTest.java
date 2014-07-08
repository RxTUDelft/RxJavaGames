package rx.test.rx2048.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rx.rx2048.model.Board;
import rx.rx2048.model.Location;
import rx.rx2048.model.Tile;

public class BoardTest {

	private Board board;
	private final int size = 2;

	@Before
	public void setUp() {
		this.board = new Board(this.size);
	}

	@Test
	public void testGetSize() {
		assertEquals(this.size, this.board.getSize());
	}

	@Test
	public void testGetLocations() {
		Iterator<Location> expected = Arrays.asList(new Location(0, 0),
				new Location(0, 1),
				new Location(1, 0),
				new Location(1, 1)).iterator();
		
		this.board.getLocations().forEach(loc -> assertEquals(expected.next(), loc));
	}

	@Test
	public void testGetAvailableLocationsEmptyBoard() {
		Iterator<Location> expected = Arrays.asList(new Location(0, 0),
				new Location(0, 1),
				new Location(1, 0),
				new Location(1, 1)).iterator();
		
		this.board.getAvailableLocations().forEach(loc -> assertEquals(expected.next(), loc));
	}
	
	@Test
	public void testGetAvailableLocations() {
		Iterator<Location> expected = Arrays.asList(new Location(0, 0),
				new Location(1, 1)).iterator();
		
		this.board.add(new Tile(2, new Location(1, 0)));
		this.board.add(new Tile(2, new Location(0, 1)));
		
		this.board.getAvailableLocations().forEach(loc -> assertEquals(expected.next(), loc));
	}
	
	@Test
	public void testGetAvailableLocationsFullBoard() {
		this.board.add(new Tile(2, new Location(0, 0)));
		this.board.add(new Tile(2, new Location(1, 0)));
		this.board.add(new Tile(2, new Location(0, 1)));
		this.board.add(new Tile(2, new Location(1, 1)));
		
		this.board.getAvailableLocations().isEmpty().forEach(Assert::assertTrue);
	}

	@Test
	public void testIsFull() {
		this.board.add(new Tile(2, new Location(0, 0)));
		this.board.add(new Tile(2, new Location(1, 0)));
		this.board.add(new Tile(2, new Location(0, 1)));
		this.board.add(new Tile(2, new Location(1, 1)));
		
		assertTrue(this.board.isFull());
	}
	
	@Test
	public void testIsNotFull() {
		this.board.add(new Tile(2, new Location(1, 0)));
		this.board.add(new Tile(2, new Location(0, 1)));
		
		assertFalse(this.board.isFull());
	}

	@Test
	public void testGetTileAtFalse() {
		assertFalse(this.board.getTileAt(new Location(1, 0)).isPresent());
	}

	@Test
	public void testGetTileAtTrueAdd() {
		Tile expected = new Tile(2, new Location(0, 1));
		this.board.add(expected);
		Optional<Tile> result = this.board.getTileAt(new Location(0, 1));
		assertTrue(result.isPresent());
		assertEquals(expected, result.get());
	}

	@Test
	public void testRemoveTile() {
		this.testGetTileAtTrueAdd();
		this.board.removeTileAt(new Location(0, 1));
		assertFalse(this.board.getTileAt(new Location(0, 1)).isPresent());
	}

	@Test
	public void testClear() {
		this.testGetTileAtTrueAdd();
		this.board.clear();
		
		this.board.getLocations().map(this.board::getTileAt).map(Optional::isPresent).forEach(Assert::assertFalse);
	}

	@Test
	public void testClearMerge() {
		Tile tile = new Tile(2, new Location(0, 1), true);
		this.board.add(tile);
		assertEquals(tile, this.board.getTileAt(new Location(0, 1)).get());
		
		this.board.clearMerge();
		assertEquals(new Tile(2, new Location(0, 1), false), this.board.getTileAt(new Location(0, 1)).get());
	}
}
