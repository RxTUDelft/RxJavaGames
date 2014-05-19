package rx.crazyeights.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import rx.crazyeights.controller.ComputerHandObserver;
import rx.crazyeights.controller.DiscardPileObserver;
import rx.crazyeights.controller.PlayerHandObserver;
import rx.crazyeights.model.Game;

public class GameView extends BorderPane {
	HBox piles;
	ImageView stockPileView, discardPileView;
	StackPane humanPlayerHand, computerPlayerHand;
	Text won, lost;

	public GameView(Game game) {
		getStylesheets().add(getClass().getResource("/Style.css").toExternalForm());
		setId("gameView");

		// menu
		GridPane menu = new GridPane();
		menu.setId("menu");
		menu.add(new Text("Crazy Eights"), 0, 0, 2, 1);
		menu.add(new Text("Won: "), 0, 1);
		won = new Text("0");
		menu.add(won, 1, 1);
		menu.add(new Text("Lost: "), 0, 2);
		lost = new Text("0");
		menu.add(lost, 1, 2);
		this.setLeft(menu);
		
		// piles
		piles = new HBox();
		piles.setId("piles");

		stockPileView = new ImageView(new Image("card_back.png"));
		stockPileView.setId("stockPile");
		stockPileView.setFitHeight(200);
		stockPileView.setPreserveRatio(true);

		discardPileView = new ImageView();
		game.getDiscardPile().subscribe(
				new DiscardPileObserver(discardPileView));
		discardPileView.setFitHeight(200);
		discardPileView.setPreserveRatio(true);

		piles.getChildren().add(stockPileView);
		piles.getChildren().add(discardPileView);
		this.setCenter(piles);

		// human player
		humanPlayerHand = new StackPane();
		humanPlayerHand.setId("humanPlayerHand");
		humanPlayerHand.setPrefWidth(800);
		humanPlayerHand.setPrefHeight(140);
		game.getHumanPlayer().subscribe(new PlayerHandObserver(game, humanPlayerHand));
		this.setBottom(humanPlayerHand);

		// computer player
		computerPlayerHand = new StackPane();
		computerPlayerHand.setId("computerPlayerHand");
		computerPlayerHand.setPrefWidth(800);
		computerPlayerHand.setPrefHeight(140);
		game.getComputerPlayer().subscribe(new ComputerHandObserver(game, this));
		this.setTop(computerPlayerHand);
	}

	public ImageView getStockPileView() {
		return stockPileView;
	}

	public ImageView getDiscardPileView() {
		return discardPileView;
	}

	public StackPane getHumanPlayerHand() {
		return humanPlayerHand;
	}

	public StackPane getComputerPlayerHand() {
		return computerPlayerHand;
	}

	public Text getWon() {
		return won;
	}
	
	public Text getLost() {
		return lost;
	}
}
