package rx.happer.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public enum Observables { ;//no class instances

	public static Observable<KeyEvent> keyPress(Scene scene) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber.onNext(event);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, handler);
			
			subscriber.add(Subscriptions.create(() -> scene.removeEventHandler(KeyEvent.KEY_PRESSED, handler)));
		});
	}
	
	public static Observable<KeyEvent> keyPress(Node node) {
		return Observable.create((Subscriber<? super KeyEvent> subscriber) -> {
			EventHandler<KeyEvent> handler = (event) -> subscriber.onNext(event);
			node.addEventHandler(KeyEvent.KEY_PRESSED, handler);
			
			subscriber.add(Subscriptions.create(() -> node.removeEventHandler(KeyEvent.KEY_PRESSED, handler)));
		});
	}
	
	public static Observable<KeyEvent> keyPress(Node node, KeyCode keyCode) {
		return keyPress(node).filter(event -> event.getCode() == keyCode);
	}
	
	public static Observable<MouseEvent> mouseClick(Node node) {
		return Observable.create((Subscriber<? super MouseEvent> subscriber) -> {
			EventHandler<MouseEvent> handler = (event) -> subscriber.onNext(event);
			node.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
			
			subscriber.add(Subscriptions.create(() -> node.removeEventHandler(MouseEvent.MOUSE_CLICKED, handler)));
		});
	}
	
	public static Observable<Number> sliderChange(Slider slider) {
        return Observable.create((Subscriber<? super Number> subscriber) -> {
            ChangeListener<? super Number> listener = (obsVal, oldVal, newVal) -> subscriber.onNext(newVal);
            slider.valueProperty().addListener(listener);
            subscriber.add(Subscriptions.create(() -> slider.valueProperty().removeListener(listener)));
        });
    }
	
	public static Observable<Toggle> toggleChange(ToggleGroup tg) {
        return Observable.create((Subscriber<? super Toggle> subscriber) -> {
            ChangeListener<? super Toggle> listener = (obsVal, oldVal, newVal) -> subscriber.onNext(newVal);
            tg.selectedToggleProperty().addListener(listener);
            subscriber.add(Subscriptions.create(() -> tg.selectedToggleProperty().removeListener(listener)));
        });
    }
	
	public static Observable<Boolean> checkBoxChange(CheckBox cb) {
        return Observable.create((Subscriber<? super Boolean> subscriber) -> {
            ChangeListener<? super Boolean> listener = (obsVal, oldVal, newVal) -> subscriber.onNext(newVal);
            cb.selectedProperty().addListener(listener);
            subscriber.add(Subscriptions.create(() -> cb.selectedProperty().removeListener(listener)));
        });
    }
}
