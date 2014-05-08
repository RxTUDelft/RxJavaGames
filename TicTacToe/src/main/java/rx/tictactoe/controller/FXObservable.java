package rx.tictactoe.controller;

import javafx.event.ActionEvent;
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

	public static Observable<ActionEvent> actionEvent(Node node) {
		return Observable.create((Subscriber<? super ActionEvent> subscriber) -> {
			EventHandler<ActionEvent> handler = (event) -> subscriber.onNext(event);
			node.addEventHandler(ActionEvent.ACTION, handler);

			subscriber.add(Subscriptions.create(() -> node.removeEventHandler(
					ActionEvent.ACTION, handler)));
		});
	}
}
