package redempt.inputscripter2.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jnativehook.keyboard.NativeKeyEvent;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import redempt.inputscripter2.Main;
import redempt.inputscripter2.utils.KeyListener;
import redempt.inputscripter2.utils.KeyCombo;

public class KeybindMaker extends Stage {
	
	private KeyCombo keybind;
	private String buttonText = null;
	
	public KeybindMaker(Consumer<KeyCombo> action) {
		this.setTitle("Select keybind");
		GridPane pane = new GridPane();
		Scene scene = new Scene(pane, 280, 130);
		this.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());
		
		Button keybindButton = new Button("Click to select keybind");
		keybindButton.setOnAction(event -> {
			keybindButton.setText("Press keybind");
			buttonText = null;
			KeyListener.addListener(new Runnable() {
				
				int maxPressed = 0;
				List<Integer> keys = null;
				
				@Override
				public void run() {
					if (KeyListener.keys.size() > maxPressed && KeybindMaker.this.isFocused()) {
						maxPressed = KeyListener.keys.size();
						if (keys == null) {
							keys = new ArrayList<>();
						}
						keys.clear();
						keys.addAll(KeyListener.keys);
					}
					if (KeyListener.keys.size() == 0 && keys != null) {
						keybind = new KeyCombo(keys);
						String text = "";
						for (int key : keys) {
							text += NativeKeyEvent.getKeyText(key) + " ";
						}
						text = text.trim();
						buttonText = text;
						KeyListener.removeListener(this);
						Platform.runLater(() -> {
							keybindButton.setText(buttonText);
						});
					}
				}
				
			});
		});
		pane.add(keybindButton, 0, 0);
		
		Button confirmButton = new Button("Confirm");
		confirmButton.setOnAction(event -> {
			if (keybind != null) {
				KeybindMaker.this.close();
				action.accept(keybind);
			}
		});
		pane.add(confirmButton, 0, 1);
		
		pane.setAlignment(Pos.CENTER_LEFT);
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(20, 20, 20, 20));
		this.setMinHeight(130);
		this.setMinWidth(280);
		this.show();
	}
	
}
