package rx.rx2048.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import rx.Observer;
import rx.rx2048.model.Score;

public class ScoreUI extends VBox implements Observer<Score> {

	private final Label scoreLabel = new Label("0");

	public ScoreUI() {
		this.setAlignment(Pos.CENTER);
		this.getStyleClass().add("vbox");
		
		Label scoreTitle = new Label("SCORE");
		scoreTitle.getStyleClass().add("titScore");
		
		this.scoreLabel.getStyleClass().add("score");
		this.getChildren().addAll(scoreTitle, this.scoreLabel);
	}
	
	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
	}

	@Override
	public void onNext(Score score) {
		this.scoreLabel.setText(String.valueOf(score.getScore()));
	}
}
