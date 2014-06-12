package rx.chainrx.controller;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class FxScheduler extends Scheduler {

	private static final FxScheduler INSTANCE = new FxScheduler();

	public static FxScheduler getInstance() {
		return INSTANCE;
	}

	@Override
	public Worker createWorker() {
		return new FxWorker();
	}

	public static class FxWorker extends Worker {
		
		private Subscription subscription = Subscriptions.create(() -> {});

		@Override
		public void unsubscribe() {
			this.subscription.unsubscribe();
		}

		@Override
		public boolean isUnsubscribed() {
			return this.subscription.isUnsubscribed();
		}
		
		@Override
		public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
			return this.schedule(action);
		}

		@Override
		public Subscription schedule(Action0 action) {
			Platform.runLater(() -> action.call());
			return this;
		}
	}
}
