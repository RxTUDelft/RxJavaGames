package rx.tudelft.pong.controller;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import rx.Observable;
import rx.functions.Actions;
import rx.observables.ConnectableObservable;
import rx.schedulers.SwingScheduler;
import rx.tudelft.pong.model.GameState;
import rx.tudelft.pong.model.paddle.Direction;
import rx.tudelft.pong.model.paddle.Inputs;

public class KeyController {

	private static final long frameRateMillis = 10L;

	public KeyController(GameState state, ConnectableObservable<Set<Integer>> keys) {
		Observable<Direction> player1Direction = keys.map(keySet -> keysToDirection(keySet,
				KeyEvent.VK_W, KeyEvent.VK_S));
		Observable<Direction> player2Direction = keys.map(keySet -> keysToDirection(keySet,
				KeyEvent.VK_UP, KeyEvent.VK_DOWN));

		Observable<Inputs> inputs = Observable.combineLatest(player1Direction, player2Direction, (
				t1, t2) -> new Inputs(t1, t2));

		Observable<GameState> sampled = inputs.sample(frameRateMillis, TimeUnit.MILLISECONDS,
				SwingScheduler.getInstance()).scan(state,
				(GameState gs, Inputs input) -> gs.step(frameRateMillis, input));

		sampled.subscribe(Actions.empty());

		keys.connect();
	}

	private static Direction keysToDirection(Set<Integer> keys, int upKey, int downKey) {
		List<Direction> res = keys.stream().filter(key -> key == upKey || key == downKey)
				.map(key -> key == upKey ? Direction.UP : Direction.DOWN)
				.collect(Collectors.toList());

		if (res.size() == 1) {
			return res.get(0);
		}
		return Direction.RESTING;
	}
}
