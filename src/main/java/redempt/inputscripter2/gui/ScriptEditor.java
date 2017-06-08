package redempt.inputscripter2.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import redempt.inputscripter2.Main;
import redempt.inputscripter2.utils.Hotkey;
import redempt.inputscripter2.utils.KeyCombo;

public class ScriptEditor extends Stage {
	
	public ScriptEditor(String string, KeyCombo keybind) {
		ScrollPane pane = new ScrollPane();
		pane.setFitToHeight(true);
		pane.setFitToWidth(true);
		Scene scene = new Scene(pane);
		
		TextArea area = new TextArea(string);
		pane.setContent(area);
		
		this.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			area.setEditable(false);
			event.consume();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Save");
			alert.setHeaderText("Save before exiting?");
			alert.setContentText("This script is not saved. Save before quitting?");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL, ButtonType.NO);
			alert.showAndWait().ifPresent(button -> {
				if (button.equals(ButtonType.CANCEL)) {
					area.setEditable(true);
				}
				if (button.equals(ButtonType.NO)) {
					ScriptEditor.this.close();
				}
				if (button.equals(ButtonType.YES)) {
					new Hotkey(keybind, Main.scriptLoader.compileScript(area.getText()));
					ScriptEditor.this.close();
				}
			});
		});
		this.setScene(scene);
		this.setTitle("Script Editor");
		this.setHeight(500);
		this.setWidth(500);
		this.show();
		this.setMinHeight(500);
		this.setMinWidth(500);
	}
	
}