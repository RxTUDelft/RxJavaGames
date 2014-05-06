package rx.pong.observables;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Shape;
import rx.Observable;
import rx.Scheduler;
import rx.pong.Main;
import rx.pong.model.Direction;
import rx.pong.model.Inputs;
import rx.pong.model.PaddleState;

public enum PongObserverUtils {
	;

	public static Observable<Integer> clock(Long frameRate, TimeUnit unit, Scheduler scheduler) {
		return Observable.interval(frameRate, unit)
				.map(x -> 1)
				.observeOn(scheduler);
	}

	public static Observable<Integer> clock(Long frameRate, TimeUnit unit) {
		return clock(frameRate, unit, FXScheduler.getInstance());
	}

	public static Observable<PaddleState> paddleController(Scene scene, Observable<Integer> clock) {
		Observable<Set<KeyCode>> keys = FXObservable.fromPressedKeys(scene);
		Observable<Direction> leftDir = keys.map(PongObserverUtils::leftToDirection);
		Observable<Direction> rightDir = keys.map(PongObserverUtils::rightToDirection);
		Observable<Inputs> inputs = Observable.combineLatest(leftDir, rightDir, Inputs::new);
		Observable<Inputs> sampled = Observable.combineLatest(clock, inputs, (c, in) -> in);
		return sampled.scan(new PaddleState(),
				(paddleState, input) -> paddleState.step(Main.frameRateMillis, input));
	}

	private static Direction keysToDirection(Set<KeyCode> keys, KeyCode upKey, KeyCode downKey) {
		List<Direction> res = keys.stream().filter(key -> key == upKey || key == downKey)
				.map(key -> key == upKey ? Direction.UP : Direction.DOWN)
				.collect(Collectors.toList());

		if (res.size() == 1) {
			return res.get(0);
		}
		return Direction.RESTING;
	}

	private static Direction leftToDirection(Set<KeyCode> keys) {
		return PongObserverUtils.keysToDirection(keys, KeyCode.W, KeyCode.S);
	}

	private static Direction rightToDirection(Set<KeyCode> keys) {
		return PongObserverUtils.keysToDirection(keys, KeyCode.UP, KeyCode.DOWN);
	}

	public static Observable<Bounds> getBounds(Observable<Integer> clock, Shape s) {
		return clock.map(i -> s.localToScene(s.getLayoutBounds()));
	}

	public static Observable<List<Boolean>> intersect(Observable<Bounds> b1, Observable<Bounds> b2) {
		return Observable.combineLatest(b1, b2, (a, b) -> a.intersects(b))
				.buffer(2, 1)
				.filter(hits -> hits.get(0) != hits.get(1));
	}
}
