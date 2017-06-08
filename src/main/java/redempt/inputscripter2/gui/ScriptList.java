package redempt.inputscripter2.gui;

import org.jnativehook.keyboard.NativeKeyEvent;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import redempt.inputscripter2.utils.Hotkey;

public class ScriptList extends Stage {
	
	private GridPane grid;
	
	public ScriptList() {
		ScrollPane pane = new ScrollPane();
		pane.setFitToHeight(true);
		pane.setFitToWidth(true);
		Scene scene = new Scene(pane);
		this.setScene(scene);
		this.setMinHeight(500);
		this.setMinWidth(300);
		pane.setMinHeight(500);
		pane.setMinWidth(300);
		this.setTitle("Scripts");
		
		grid = new GridPane();
		pane.setContent(grid);
		grid.setVgap(5);
		
		refresh();
		
		this.show();
	}
	
	private void refresh() {
		grid.getChildren().clear();
		for (Hotkey hotkey : Hotkey.getHotkeys()) {
			String name = "";
			for (int key : hotkey.getKeyCombo().getKeys()) {
				name += NativeKeyEvent.getKeyText(key) + " ";
			}
			name = name.trim();
			Button button = new Button(name);
			button.setStyle("-fx-font: 18px \"Arial\"");
			button.setMinHeight(50);
			button.setMinWidth(290);
			button.setOnAction(event -> {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Select action");
				alert.setHeaderText("Select action");
				alert.setContentText("What do you want to change?");
				alert.getButtonTypes().clear();
				alert.getButtonTypes().setAll(new ButtonType("Script"), new ButtonType("Keybind"), new ButtonType("Delete"), ButtonType.CANCEL);
				alert.showAndWait().ifPresent(action -> {
					if (action.getText().equals("Script")) {
						new ScriptEditor(hotkey.getScript().getSource(), hotkey.getKeyCombo()).addEventHandler(WindowEvent.WINDOW_HIDDEN, closed -> {
							refresh();
						});
					}
					if (action.getText().equals("Keybind")) {
						new KeybindMaker(keybind -> {
							hotkey.unregister();
							new Hotkey(keybind, hotkey.getScript());
							refresh();
						});
					}
					if (action.getText().equals("Delete")) {
						Alert confirm = new Alert(AlertType.CONFIRMATION);
						confirm.setTitle("Confirm delete");
						confirm.setHeaderText("Confirm delete");
						confirm.setContentText("Are you sure you want to delete this?");
						confirm.getButtonTypes().clear();
						confirm.getButtonTypes().setAll(ButtonType.CANCEL, ButtonType.YES);
						confirm.showAndWait().ifPresent(buttonType -> {
							if (buttonType.equals(ButtonType.YES)) {
								hotkey.unregister();
								refresh();
							}
						});
					}
				});
			});
			grid.add(button, 0, grid.getChildren().size());
		}
	}
	
}
