package rx.chainrx.controller;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.chainrx.model.Ball;
import rx.chainrx.model.CircleState;
import rx.chainrx.model.PlayerCircle;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;

public enum Observables {
	;// no class instances

	public static ConnectableObservable<Integer> clock(Long frameRate, TimeUnit unit,
			Scheduler scheduler) {
		return Observable.interval(frameRate, unit).map(x -> 1)
				.observeOn(scheduler).publish();
	}

	public static ConnectableObservable<Integer> clock(Long frameRate) {
		return clock(frameRate, TimeUnit.MILLISECONDS,
				FxScheduler.getInstance());
	}

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
	
	public static Observable<MouseEvent> mouseMove(Node node) {
		return Observable
				.create((Subscriber<? super MouseEvent> subscriber) -> {
					EventHandler<MouseEvent> handler = (event) -> subscriber
							.onNext(event);
					node.addEventHandler(MouseEvent.MOUSE_MOVED, handler);

					subscriber.add(Subscriptions.create(() -> node
							.removeEventHandler(MouseEvent.MOUSE_MOVED,
									handler)));
		});
	}
	
	public static Observable<MouseEvent> movingPlayerCircle(Node node, PlayerCircle playerCircle) {
		return mouseMove(node).filter(e -> playerCircle.getState() == CircleState.MOVING);
	}

	public static Observable<Ball> movingBalls(Observable<Integer> clock, Ball ball) {
		return clock
				.map(i -> ball)
				.filter(b -> b.getState() == CircleState.MOVING);
	}

	public static Observable<Ball> collisions(Observable<Integer> clock, Set<Ball> balls, PlayerCircle playerCircle) {
		return Observable.create((Subscriber<? super Ball> subscriber) -> {
			clock.subscribe(i -> {
				balls.stream()
					.filter(b1 -> b1.getState() == CircleState.MOVING)
					.forEach(b1 ->
					{
						boolean collisionWithPlayerCircle = 
								playerCircle.getState() == CircleState.EXPANDED && 
								circlesIntersect(b1.getView(), playerCircle.getView());
						
						boolean collisionWithOtherBall = balls.stream()
							.filter(b2 -> ! b1.equals(b2))
							.filter(b2 -> b2.getState() == CircleState.EXPANDED)
							.anyMatch(b2 -> circlesIntersect(b1.getView(), b2.getView()));
							
						if(collisionWithPlayerCircle || collisionWithOtherBall) {
							subscriber.onNext(b1);
						}
					});
			});
		});
	}
	
	
	public static Observable<Boolean> endLevel(Observable<Integer> clock, PlayerCircle playerCircle, Set<Ball> balls) {
		return clock.filter(i -> playerCircle.getState() == CircleState.DISAPPEARED)
				.map(i -> balls.stream().allMatch(ball -> ball.getState() != CircleState.EXPANDED))
				.filter(bool -> bool == true)
				.first();
	}
	
	
	public static Observable<Long> disappearTimer() {
		return Observable.timer(3000, TimeUnit.MILLISECONDS);
	}
	
	private static boolean circlesIntersect(Circle c1, Circle c2) {
		Bounds bounds1 = c1.localToScene(c1.getLayoutBounds());
		double x1 = bounds1.getMinX() + bounds1.getWidth() / 2;
		double y1 = bounds1.getMinY() + bounds1.getHeight() / 2;
		double c1Radius = bounds1.getWidth() / 2;

		Bounds bounds2 = c2.localToScene(c2.getLayoutBounds());
		double x2 = bounds2.getMinX() + bounds2.getWidth() / 2;
		double y2 = bounds2.getMinY() + bounds2.getHeight() / 2;
		double c2Radius = bounds2.getWidth() / 2;

		double distance = Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));

		if (distance <= c1Radius + c2Radius) {
			return true;
		} else {
			return false;
		}
	}
}
