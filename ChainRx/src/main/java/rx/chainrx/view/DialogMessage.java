package rx.chainrx.view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DialogMessage extends VBox {
	Text txt;
	Button btn;
	
	public DialogMessage(String message, String btnText) {
		super(10);
		setId("dialog");
		
		txt = new Text(message);
		txt.setId("dialogMessage");
		this.getChildren().add(txt);
		
		btn = new Button(btnText);
		this.getChildren().add(btn);
	}
	
	public Button getButton() {
		return btn;
	}
	
}
