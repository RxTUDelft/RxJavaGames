package rx.tictactoe.controller;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public enum FXObservable {
	;

	public static Observable<MouseEvent> mouseClick(Node node) {
		return Observable.create((Subscriber<? super MouseEvent> subscriber) -> {
			EventHandler<MouseEvent> handler = (event) -> subscriber.onNext(event);
			node.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);

			subscriber.add(Subscriptions.create(() -> node.removeEventHandler(
					MouseEvent.MOUSE_CLICKED, handler)));
		});
	}
}
