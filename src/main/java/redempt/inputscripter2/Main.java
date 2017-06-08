package redempt.inputscripter2;

import java.io.IOException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import redempt.inputscripter2.gui.GroupList;
import redempt.inputscripter2.gui.KeybindMaker;
import redempt.inputscripter2.gui.ScriptEditor;
import redempt.inputscripter2.gui.ScriptList;
import redempt.inputscripter2.gui.docs.DocsWindow;
import redempt.inputscripter2.script.ScriptLoader;
import redempt.inputscripter2.utils.Hotkey;
import redempt.inputscripter2.utils.KeyListener;
import redempt.inputscripter2.utils.MouseListener;

public class Main extends Application {
	
	public static ScriptLoader scriptLoader = new ScriptLoader();
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage stage) {
		try {
			KeyListener.register();
			MouseListener.register();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		{
			Stage loading = new Stage();
			loading.setTitle("Loading");
			loading.setMinHeight(60);
			loading.setMinWidth(250);
			loading.setHeight(60);
			loading.setWidth(250);
			GridPane pane = new GridPane();
			pane.setAlignment(Pos.CENTER);
			Scene scene = new Scene(pane);
			loading.setScene(scene);
			
			Label label = new Label("Loading keybinds and scripts...");
			loading.show();
			label.setAlignment(Pos.CENTER);
			pane.add(label, 0, 0);
			try {
				Hotkey.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			loading.close();
		}
		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
			System.out.println("Saving and exiting...");
			try {
				Hotkey.save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e) {
				e.printStackTrace();
			}
			System.exit(0);
		});
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setHgap(10);
		pane.setVgap(10);
		Scene scene = new Scene(pane, 300, 150);
		scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());
		
		Button newKeybind = new Button("New script");
		newKeybind.setOnAction(event -> 
			new KeybindMaker(keybind -> {
				new ScriptEditor("", keybind);
			}
		));
		pane.add(newKeybind, 0, 0);
		
		Button keybindList = new Button("Scripts");
		keybindList.setOnAction(event -> {
			new ScriptList();
		});
		pane.add(keybindList, 1, 0);
		
		Button tutorials = new Button("Tutorials");
		tutorials.setOnAction(event -> {
			new DocsWindow();
		});
		pane.add(tutorials, 0, 1);
		
		Button groups = new Button("Groups");
		groups.setOnAction(event -> {
			new GroupList();
		});
		pane.add(groups, 1, 1);
		
		stage.setScene(scene);
		stage.setTitle("InputScripter");
		stage.setHeight(150);
		stage.setWidth(300);
		stage.show();
		stage.setMinHeight(150);
		stage.setMinWidth(300);
	}
	
}