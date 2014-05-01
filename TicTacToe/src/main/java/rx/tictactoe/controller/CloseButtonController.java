package rx.tictactoe.controller;

import javafx.scene.input.MouseEvent;
import rx.Observer;

public class CloseButtonController implements Observer<MouseEvent> {

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(MouseEvent t) {
		System.exit(0);
	}
}
