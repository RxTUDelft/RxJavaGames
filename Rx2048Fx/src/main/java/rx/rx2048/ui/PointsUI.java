package rx.rx2048.ui;

import rx.Observer;
import rx.rx2048.model.Points;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PointsUI extends Label implements Observer<Points> {
	
	public PointsUI() {
		super();
		this.getStyleClass().add("points");
	}

	private Timeline animateScore(Points points) {
        final Timeline timeline = new Timeline();
        this.setText("+" + points.getPoints());
        this.setOpacity(1);
        this.setLayoutX(400);
        this.setLayoutY(20);
        final KeyValue kvO = new KeyValue(this.opacityProperty(), 0);
        final KeyValue kvY = new KeyValue(this.layoutYProperty(), 100);

        Duration animationDuration = Duration.millis(600);
        final KeyFrame kfO = new KeyFrame(animationDuration, kvO);
        final KeyFrame kfY = new KeyFrame(animationDuration, kvY);

        timeline.getKeyFrames().add(kfO);
        timeline.getKeyFrames().add(kfY);

        return timeline;
    }

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
	}

	@Override
	public void onNext(Points points) {
		this.animateScore(points).play();
	}
}
