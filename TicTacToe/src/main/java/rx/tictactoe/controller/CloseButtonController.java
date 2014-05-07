package rx.tictactoe.controller;

import javafx.event.ActionEvent;
import rx.Observer;

public class CloseButtonController implements Observer<ActionEvent> {

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(ActionEvent t) {
		System.exit(0);
	}
}
