package rx.flappyBirds;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public enum SpacebarObservable { ;//no class instances

	public static Observable<KeyEvent> keyPress(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber.onNext(event);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
			
			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler)));
		});
	}

	public static Observable<KeyEvent> spaceBar(Scene scene) {
		return keyPress(scene).filter(event -> event.getCode() == KeyCode.SPACE);
	}
}
