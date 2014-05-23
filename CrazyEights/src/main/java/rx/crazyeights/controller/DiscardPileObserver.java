package rx.crazyeights.controller;

import javafx.scene.image.ImageView;
import rx.Observer;
import rx.crazyeights.model.Card;
import rx.crazyeights.model.DiscardPile;

public class DiscardPileObserver implements Observer<DiscardPile> {

	private ImageView discardPileView;
	
	public DiscardPileObserver(ImageView discardPileView) {
		this.discardPileView = discardPileView;
	}
	
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(DiscardPile dp) {
		Card top = dp.getCardAtTop();
		discardPileView.setImage(top.getImage());
	}
	
}
