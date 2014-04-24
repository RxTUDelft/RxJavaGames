package rx.pong.observables;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Shape;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Func2;
import rx.pong.Main;
import rx.pong.model.Direction;
import rx.pong.model.Inputs;
import rx.pong.model.PaddleState;
import rx.subscriptions.Subscriptions;

public enum FXObservable {
	;

	public static Observable<KeyEvent> fromKeyEventsOf(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			scene.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> subscriber.onNext(event));
			scene.addEventHandler(KeyEvent.KEY_RELEASED, (event) -> subscriber.onNext(event));
			scene.addEventHandler(KeyEvent.KEY_TYPED, (event) -> subscriber.onNext(event));

			subscriber.add(Subscriptions.create(() -> {
				if (Platform.isFxApplicationThread()) {
					scene.removeEventHandler(KeyEvent.KEY_PRESSED,
							(event) -> subscriber.onNext(event));
					scene.removeEventHandler(KeyEvent.KEY_RELEASED,
							(event) -> subscriber.onNext(event));
					scene.removeEventHandler(KeyEvent.KEY_TYPED,
							(event) -> subscriber.onNext(event));
				}
					else {
						FXScheduler.getInstance().schedule(
								inner -> {
									scene.removeEventHandler(KeyEvent.KEY_PRESSED,
											(event) -> subscriber.onNext(event));
									scene.removeEventHandler(KeyEvent.KEY_RELEASED,
											(event) -> subscriber.onNext(event));
									scene.removeEventHandler(KeyEvent.KEY_TYPED,
											(event) -> subscriber.onNext(event));
								});
					}
				}));
		});
	}

	public static Observable<Set<KeyCode>> fromPressedKeys(Scene scene) {
		class CollectKeys implements Func2<Set<KeyCode>, KeyEvent, Set<KeyCode>> {

			@Override
			public Set<KeyCode> call(Set<KeyCode> pressedKeys, KeyEvent event) {
				Set<KeyCode> afterEvent = new HashSet<>(pressedKeys);
				if (event.getEventType() == KeyEvent.KEY_PRESSED) {
					afterEvent.add(event.getCode());
				}
				else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
					afterEvent.remove(event.getCode());
				}
				else {
					// nothing to do
				}
				return afterEvent;
			}
		}
		;

		Observable<KeyEvent> filteredKeyEvents = fromKeyEventsOf(scene).filter(
				event -> event.getEventType() == KeyEvent.KEY_PRESSED
						|| event.getEventType() == KeyEvent.KEY_RELEASED);
		return filteredKeyEvents.scan(Collections.<KeyCode> emptySet(), new CollectKeys());
	}

	public static Observable<Integer> clock(Long frameRate, TimeUnit unit, Scheduler scheduler) {
		return Observable.timer(0L, frameRate, unit)
				.map(x -> 1)
				.observeOn(scheduler);
	}

	public static Observable<Integer> clock(Long frameRate, TimeUnit unit) {
		return clock(frameRate, unit, FXScheduler.getInstance());
	}

	public static Observable<PaddleState> paddleController(Scene scene, Observable<Integer> clock) {
		Observable<Set<KeyCode>> keys = FXObservable.fromPressedKeys(scene);
		Observable<Direction> leftDir = keys.map(FXObservable::leftToDirection);
		Observable<Direction> rightDir = keys.map(FXObservable::rightToDirection);
		Observable<Inputs> inputs = Observable.combineLatest(leftDir, rightDir, Inputs::new);
		// Observable<Inputs> sampled = inputs.sample(clock);
		Observable<Inputs> sampled = inputs.sample(Main.frameRateMillis, TimeUnit.MILLISECONDS,
				FXScheduler.getInstance());
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
		return FXObservable.keysToDirection(keys, KeyCode.W, KeyCode.S);
	}

	private static Direction rightToDirection(Set<KeyCode> keys) {
		return FXObservable.keysToDirection(keys, KeyCode.UP, KeyCode.DOWN);
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
