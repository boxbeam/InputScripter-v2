package redempt.inputscripter2.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
		area.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() != KeyCode.TAB) {
				return;
			}
			String[] lines = split(area.getText(), '\n');
			int lineNumber = 0;
			int pos = 0;
			for (char c : area.getText().toCharArray()) {
				if (c == '\n') {
					lineNumber ++;
				}
				pos++;
				if (pos >= area.getCaretPosition()) {
					break;
				}
			}
			if (lineNumber > 0) {
				String previousLine = lines[lineNumber - 1];
				int tabAmount = 0;
				for (char c : previousLine.toCharArray()) {
					if (c == '\t') {
						tabAmount++;
					} else {
						break;
					}
				}
				if (lineNumber >= lines.length) {
					return;
				}
				String currentLine = lines[lineNumber];
				int currentLineTab = 0;
				for (char c : currentLine.toCharArray()) {
					if (c == '\t') {
						currentLineTab++;
					} else {
						break;
					}
				}
				if (currentLineTab >= tabAmount) {
					return;
				}
				int diff = tabAmount - currentLineTab;
				while (currentLineTab < tabAmount) {
					currentLine = "\t" + currentLine;
					currentLineTab++;
				}
				lines[lineNumber] = currentLine;
				String combine = "";
				for (String line : lines) {
					combine += line;
				}
				int caret = area.getCaretPosition();
				area.setText(combine);
				area.positionCaret(caret + diff);
				event.consume();
			}
		});
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
					try {
						Hotkey.save(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	
	public String[] split(String string, char split) {
		List<String> strings = new ArrayList<>();
		String combine = "";
		char[] chars = string.toCharArray();
		for (int pos = 0; pos < string.length(); pos++) {
			combine += chars[pos];
			if (chars[pos] == split) {
				strings.add(combine);
				combine = "";
			}
		}
		strings.add(combine);
		return strings.toArray(new String[strings.size()]);
	}
	
}