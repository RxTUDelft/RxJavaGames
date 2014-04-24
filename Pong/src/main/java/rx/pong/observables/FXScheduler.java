package rx.pong.observables;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

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
	public Subscription schedule(Action1<Inner> action, long delayTime, TimeUnit unit) {
		this.inner.schedule(action, delayTime, unit);
		return this.inner;
	}

	public static class FXInner extends Inner {

		private final CompositeSubscription subscription = new CompositeSubscription();

		@Override
		public void unsubscribe() {
			this.subscription.unsubscribe();
		}

		@Override
		public boolean isUnsubscribed() {
			return this.subscription.isUnsubscribed();
		}

		@Override
		public void schedule(Action1<Inner> action, long delayTime, TimeUnit unit) {
			this.schedule(t1 -> {
				if (!this.isUnsubscribed()) {
					try {
						Thread.sleep(TimeUnit.MILLISECONDS.convert(delayTime, unit));
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (!this.isUnsubscribed()) {
					action.call(t1);
				}
			});
		}

		@Override
		public void schedule(Action1<Inner> action) {
			if (!this.isUnsubscribed()) {
				Platform.runLater(() -> {
					if (!this.isUnsubscribed()) {
						action.call(FXInner.this);
					}
				});
			}
		}
	}
}
