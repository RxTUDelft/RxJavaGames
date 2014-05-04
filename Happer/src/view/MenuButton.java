package view;

import javafx.scene.control.Button;

public class MenuButton extends Button {

	public MenuButton(String txt, boolean enabled) {
		super(txt);
		
		setPrefWidth(100);
		setFocusTraversable(false);
		
		if(! enabled) {
			setDisable(true);
		}
	}
	
}
