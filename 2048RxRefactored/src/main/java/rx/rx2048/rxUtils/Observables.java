package rx.rx2048.rxUtils;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.SwipeEvent;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public enum Observables {
	;// no class instances

	public static Observable<KeyEvent> keyPress(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber
					.onNext(event);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);

			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(
					KeyEvent.KEY_PRESSED, handler)));
		});
	}

	public static Observable<SwipeEvent> swipe(Scene scene) {
		return Observable.create((Subscriber<? super SwipeEvent> subscriber) -> {
			EventHandler<SwipeEvent> handler = subscriber::onNext;
			scene.addEventHandler(SwipeEvent.ANY, handler);
			
			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(
					SwipeEvent.ANY, handler)));
		});
	} 
}
