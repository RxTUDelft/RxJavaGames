package rx.test.rx2048.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rx.rx2048.model.Direction;
import rx.rx2048.model.Location;

public class LocationTest {

	private Location location;
	private final int x = 2;
	private final int y = 3;

	@Before
	public void setUp() {
		this.location = new Location(this.x, this.y);
	}

	@Test
	public void testGetX() {
		assertEquals(this.x, this.location.getX());
	}

	@Test
	public void testGetY() {
		assertEquals(this.y, this.location.getY());
	}

	@Test
	public void testMoveLeft() {
		Location expected = new Location(this.x - 1, this.y);
		assertEquals(expected, this.location.move(Direction.LEFT));
	}

	@Test
	public void testMoveRight() {
		Location expected = new Location(this.x + 1, this.y);
		assertEquals(expected, this.location.move(Direction.RIGHT));
	}

	@Test
	public void testMoveUp() {
		Location expected = new Location(this.x, this.y - 1);
		assertEquals(expected, this.location.move(Direction.UP));
	}

	@Test
	public void testMoveDown() {
		Location expected = new Location(this.x, this.y + 1);
		assertEquals(expected, this.location.move(Direction.DOWN));
	}

	@Test
	public void testIsValidForXSmallerThan0() {
		Location out = new Location(-1, 1);
		assertFalse(out.isValidFor(2));
	}

	@Test
	public void testIsValidForXBiggerThanSize() {
		Location out = new Location(3, 0);
		assertFalse(out.isValidFor(2));
	}

	@Test
	public void testIsValidForYSmallerThan0() {
		Location out = new Location(0, -1);
		assertFalse(out.isValidFor(2));
	}

	@Test
	public void testIsValidForYBiggerThanSize() {
		Location out = new Location(0, 3);
		assertFalse(out.isValidFor(2));
	}

	@Test
	public void testIsValid() {
		Location out = new Location(2, 2);
		assertTrue(out.isValidFor(3));
	}
}
