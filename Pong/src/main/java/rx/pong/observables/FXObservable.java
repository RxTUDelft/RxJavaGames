package rx.pong.observables;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
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
		Func2<Set<KeyCode>, KeyEvent, Set<KeyCode>> func = (pressedKeys, event) -> {
			Set<KeyCode> afterEvent = new HashSet<>(pressedKeys);
			if (event.getEventType() == KeyEvent.KEY_PRESSED) {
				afterEvent.add(event.getCode());
			}
				else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
					afterEvent.remove(event.getCode());
				}
				return afterEvent;
			};

		return fromKeyEventsOf(scene)
				.filter(event -> event.getEventType() == KeyEvent.KEY_PRESSED
						|| event.getEventType() == KeyEvent.KEY_RELEASED)
				.scan(Collections.<KeyCode> emptySet(), func);
	}
}
