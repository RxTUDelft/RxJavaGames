package rx.platformer.controller;

import java.util.concurrent.TimeUnit;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;

public enum Observables {
	;// no class instances

	public static ConnectableObservable<Integer> clock(Long frameRate,
			TimeUnit unit, Scheduler scheduler) {
		return Observable.interval(frameRate, unit).map(x -> 1)
				.observeOn(scheduler).publish();
	}

	public static ConnectableObservable<Integer> clock(Long frameRate) {
		return clock(frameRate, TimeUnit.MILLISECONDS,
				FxScheduler.getInstance());
	}

	public static Observable<KeyEvent> keyPress(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber
					.onNext(event);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);

			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(
					KeyEvent.KEY_PRESSED, handler)));
		});
	}

	public static Observable<KeyEvent> keyPress(Scene scene, KeyCode keyCode) {
		return keyPress(scene).filter(event -> event.getCode() == keyCode);
	}
	
	public static Observable<KeyEvent> keyRelease(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber
					.onNext(event);
			scene.addEventHandler(KeyEvent.KEY_RELEASED, handler);

			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(
					KeyEvent.KEY_RELEASED, handler)));
		});
	}
	
	public static Observable<KeyEvent> keyRelease(Scene scene, KeyCode keyCode) {
		return keyRelease(scene).filter(event -> event.getCode() == keyCode);
	}
}
