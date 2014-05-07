package rx.happer.controller;

import java.util.concurrent.TimeUnit;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import rx.functions.Action2;
import rx.happer.model.Direction;
import rx.happer.model.Game;
import rx.happer.model.GameSettings;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.happer.view.GameView;
import rx.happer.view.TextBox;

public class GameController {
	private Subscription intervalSubscription;
	private Subscription[] arrowKeySubscriptions;
	
	public GameController(Game game, GameView gameView) {
		
		Action2<KeyEvent, Direction> action = (event, dir) -> {
			game.resetDistances();
			game.getPacman().move(dir);
		};
		
		arrowKeySubscriptions = new Subscription[4];
		
		// pacman key observables
		arrowKeySubscriptions[0] = Observables.keyPress(gameView, KeyCode.RIGHT)
				.subscribe(event -> action.call(event, Direction.RIGHT));

		arrowKeySubscriptions[1] = Observables.keyPress(gameView, KeyCode.LEFT)
				.subscribe(event -> action.call(event, Direction.LEFT));

		arrowKeySubscriptions[2] = Observables.keyPress(gameView, KeyCode.UP)
				.subscribe(event -> action.call(event, Direction.UP));

		arrowKeySubscriptions[3] = Observables.keyPress(gameView, KeyCode.DOWN)
				.subscribe(event -> action.call(event, Direction.DOWN));

		// happer timer observable
		Observable<Long> interval = Observable.interval(
				GameSettings.happerSpeed, TimeUnit.MILLISECONDS).observeOn(
				FXScheduler.getInstance());

		intervalSubscription = interval.subscribe(event -> game.getHapper().move());

		// end game observable
		Observer<Boolean> endGameObserver = new Observer<Boolean>() {

			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(Boolean won) {
				unsubscribeInterval();
				unsubscribeArrowKeys();
				
				StackPane pane = new StackPane();
				pane.setId("endGamePane");
				
				TextBox tv = null;
				if(won) {
					tv = new TextBox("You win!", true);
				} else {
					tv = new TextBox("You lose!", false);
				}
				pane.getChildren().add(tv);
				gameView.getChildren().add(pane);
			}
		};

		game.getHapper().subscribeOnEndGame(endGameObserver);

	}

	public void unsubscribeInterval() {
		intervalSubscription.unsubscribe();
	}
	
	public void unsubscribeArrowKeys() {
		for(Subscription s : arrowKeySubscriptions) {
			s.unsubscribe();
		}
	}
}
