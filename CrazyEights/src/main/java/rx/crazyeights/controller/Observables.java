package rx.crazyeights.controller;

import java.util.concurrent.TimeUnit;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public enum Observables {
	;// no class instances

	public static Observable<MouseEvent> mouseClick(Node node) {
		return Observable
				.create((Subscriber<? super MouseEvent> subscriber) -> {
					EventHandler<MouseEvent> handler = (event) -> subscriber
							.onNext(event);
					node.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);

					subscriber.add(Subscriptions.create(() -> node
							.removeEventHandler(MouseEvent.MOUSE_CLICKED,
									handler)));
				});
	}

	public static Observable<MouseEvent> mouseDoubleClick(Node node) {
		return Observable
				.create((Subscriber<? super MouseEvent> subscriber) -> {
					EventHandler<MouseEvent> handler = (event) -> {
						if (event.getClickCount() == 2) {
							subscriber.onNext(event);
						}
					};
					node.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);

					subscriber.add(Subscriptions.create(() -> node
							.removeEventHandler(MouseEvent.MOUSE_CLICKED,
									handler)));
				});
	}
	
	
	public static Observable<Long> delay(int milliseconds) {
		return Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
				.observeOn(new FxScheduler());
	}
	
}
