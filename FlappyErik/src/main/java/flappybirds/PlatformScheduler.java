package flappybirds;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class PlatformScheduler extends Scheduler {
	
	private final Inner inner;
	
	public PlatformScheduler() {
		super();
		this.inner = new MyInner();
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
	
	public static class MyInner extends Inner {

		private Subscription subscription = Subscriptions.empty();

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
				if (!isUnsubscribed()) {
					try {
						Thread.sleep(TimeUnit.MILLISECONDS.convert(delayTime, unit));
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (!isUnsubscribed()) {
					action.call(t1);
				}
			});
		}

		@Override
		public void schedule(Action1<Inner> action) {
			if (!isUnsubscribed()) {
				Platform.runLater(() -> {
					if (!isUnsubscribed()) {
						action.call(MyInner.this);
					}
				});
			}
		}
	}
}