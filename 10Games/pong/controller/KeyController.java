package controller;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import model.Direction;
import model.GameState;
import model.Inputs;
import rx.Observable;
import rx.concurrency.SwingScheduler;
import rx.observables.ConnectableObservable;

public class KeyController extends Observable<GameState> {

	private static final long frameRateMillis = 10L;

	public KeyController(GameState initState, ConnectableObservable<Set<Integer>> keys) {
		super(subscriber -> {
			Observable<Direction> player1Direction = keys.map(keySet -> keysToDirection(keySet,
					KeyEvent.VK_W, KeyEvent.VK_S));
			Observable<Direction> player2Direction = keys.map(keySet -> keysToDirection(keySet,
					KeyEvent.VK_UP, KeyEvent.VK_DOWN));

			Observable<Inputs> inputs = Observable.combineLatest(player1Direction,
					player2Direction, (t1, t2) -> new Inputs(t1, t2));

			Observable<Inputs> sampled = inputs.sample(frameRateMillis, TimeUnit.MILLISECONDS,
					SwingScheduler.getInstance());
			
			Observable<GameState> scanned = sampled.scan(initState,
					(GameState gs, Inputs input) -> gs.step(frameRateMillis, input));
			
			return scanned.subscribe(subscriber);
		});

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
