package rx.rx2048.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopUI extends HBox {

	private final Label rxSubtitle = new Label("RX");
	private final Label title = new Label("2048");
	private final Label fxSubtitle = new Label("FX");
	private final HBox hFill = new HBox();
	
	public TopUI() {
		super(0);
		this.rxSubtitle.getStyleClass().add("rxsubtitle");
		this.title.getStyleClass().add("title");
		this.fxSubtitle.getStyleClass().add("fxsubtitle");
		HBox.setHgrow(this.hFill, Priority.ALWAYS);
		this.hFill.setAlignment(Pos.CENTER);
		
		this.getChildren().addAll(this.rxSubtitle, this.title, this.fxSubtitle, this.hFill);
		this.setMinSize(Constants.GRID_WIDTH.get(), Constants.TOP_HEIGHT.get());
		this.setPrefSize(Constants.GRID_WIDTH.get(), Constants.TOP_HEIGHT.get());
		this.setMaxSize(Constants.GRID_WIDTH.get(), Constants.TOP_HEIGHT.get());
	}
}
