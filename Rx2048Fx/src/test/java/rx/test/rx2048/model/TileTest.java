package rx.test.rx2048.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import rx.rx2048.model.Location;
import rx.rx2048.model.Tile;

public class TileTest {

	private Tile tile;
	private final Location location = new Location(0, 0);
	
	@Before
	public void setUp() {
		this.tile = new Tile(2, this.location);
	}

	@Test
	public void testGetValue() {
		assertEquals(new Integer(2), this.tile.getValue());
	}

	@Test
	public void testGetLocation() {
		assertEquals(this.location, this.tile.getLocation());
	}

	@Test
	public void testMerge1() {
		Tile other = new Tile(2, new Location(0, 3));
		assertEquals(new Tile(4, new Location(0, 0)), this.tile.merge(other));
	}
	
	@Test
	public void testMerge2() {
		Tile other = new Tile(2, new Location(0, 3));
		assertEquals(new Tile(4, new Location(0, 3)), other.merge(this.tile));
	}

	@Test(expected = AssertionError.class)
	public void testMergeErrorOtherValue() {
		Tile other = new Tile(4, new Location(0, 3));
		this.tile.merge(other);
	}
	
	@Test(expected = AssertionError.class)
	public void testMergeErrorWrongLocation() {
		Tile other = new Tile(2, new Location(1, 2));
		this.tile.merge(other);
	}
}
