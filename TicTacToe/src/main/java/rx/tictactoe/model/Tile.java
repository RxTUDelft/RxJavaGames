package rx.tictactoe.model;

import java.util.Objects;
import java.util.Optional;

import rx.Observable;
import rx.subjects.PublishSubject;

public final class Tile extends Observable<Tile> {

	private final int x;
	private final int y;
	private Optional<Sprite> sprite;

	private final PublishSubject<Tile> subject;

	public Tile(int x, int y) {
		this(x, y, PublishSubject.create());
	}
	
	private Tile(int x, int y, PublishSubject<Tile> subject) {
		super(subscriber -> subject.subscribe(subscriber));
		this.x = x;
		this.y = y;
		this.sprite = Optional.empty();
		this.subject = subject;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Optional<Sprite> getSprite() {
		return this.sprite;
	}

	public void setSprite(Optional<Sprite> sprite) {
		this.sprite = sprite;
		
		this.subject.onNext(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Tile) {
			Tile that = (Tile) other;
			return Objects.equals(this.x, that.x)
					&& Objects.equals(this.y, that.y)
					&& Objects.equals(this.sprite, that.sprite);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y, this.sprite);
	}

	@Override
	public String toString() {
		return "<Tile[" + this.x + ", " + this.y + ", " + this.sprite + "]>";
	}
}
