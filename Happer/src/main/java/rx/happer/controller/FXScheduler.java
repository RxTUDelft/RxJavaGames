package rx.happer.controller;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class FXScheduler extends Scheduler {

	private static final FXScheduler INSTANCE = new FXScheduler();

	private final Inner inner = new FXInner();

	public static FXScheduler getInstance() {
		return INSTANCE;
	}

	private FXScheduler() {
	}

	@Override
	public Subscription schedule(Action1<Inner> action) {
		this.inner.schedule(action);
		return this.inner;
	}

	@Override
	public Subscription schedule(Action1<Inner> action, long delayTime,
			TimeUnit unit) {
		this.inner.schedule(action, delayTime, unit);
		return this.inner;
	}

	public class FXInner extends Inner {

		// private Subscription subscription = Subscriptions.empty();
		//private final CompositeSubscription subscription = new CompositeSubscription();
		private Subscription subscription = Subscriptions.create(() -> {
		});

		@Override
		public void unsubscribe() {
			this.subscription.unsubscribe();
		}

		@Override
		public boolean isUnsubscribed() {
			return this.subscription.isUnsubscribed();
		}

		@Override
		public void schedule(Action1<Inner> action, long delayTime,	TimeUnit unit) {
			schedule(action);
		}

		@Override
		public void schedule(Action1<Inner> action) {
			Platform.runLater(() -> {
				action.call(FXInner.this);
			});
		}
	}
}
