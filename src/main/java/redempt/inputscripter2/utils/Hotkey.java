package redempt.inputscripter2.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import redempt.inputscripter2.Main;
import redempt.inputscripter2.script.Script;

public class Hotkey {
	
	private static List<Hotkey> hotkeys = new CopyOnWriteArrayList<>();
	private static String dir = "default";
	
	private Runnable action;
	private KeyCombo keyCombo;
	private Script script;
	
	public static List<Hotkey> getHotkeys() {
		return hotkeys;
	}
	
	public static File getCurrentDir() {
		try {
			return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setDir(String dir) throws IOException {
		save(true);
		Hotkey.dir = dir;
		load();
	}
	
	public static void save(boolean clear) throws IOException {
		File file = new File(getCurrentDir(), dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		for (File f : file.listFiles()) {
			if (f.getName().endsWith(".insc2")) {
				f.delete();
			}
		}
		int number = 0;
		for (Hotkey hotkey : hotkeys) {
			File f = new File(file, number + ".insc2");
			f.createNewFile();
			FileWriter writer = new FileWriter(f);
			writer.write(hotkey.getKeyCombo().toString() + "\n" + hotkey.getScript().getSource());
			writer.close();
			number++;
		}
		if (clear) {
			for (Hotkey hotkey : hotkeys) {
				hotkey.unregister();
			}
			hotkeys.clear();
		}
	}
	
	public static void load() throws IOException {
		for (Hotkey hotkey : hotkeys) {
			hotkey.unregister();
		}
		hotkeys.clear();
		File file = new File(getCurrentDir(), dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		for (File f : file.listFiles()) {
			if (f.getName().endsWith(".insc2")) {
				FileReader read = new FileReader(f);
				BufferedReader reader = new BufferedReader(read);
				String firstLine = reader.readLine();
				String script = "";
				KeyCombo keybind = KeyCombo.fromString(firstLine);
				String next = "";
				while ((next = reader.readLine()) != null) {
					script += next + "\n";
				}
				script = script.replaceAll("\n$", "");
				reader.close();
				new Hotkey(keybind, Main.scriptLoader.compileScript(script));
			}
		}
	}
	
	public Hotkey(KeyCombo keybind, Script script) {
		this.keyCombo = keybind;
		this.script = script;
		action = () -> {
			if (keybind.isPressed()) {
				if (script.isRunning()) {
					script.terminate();
				} else {
					script.runAsync();
				}
			}
		};
		for (Hotkey hotkey : getHotkeys()) {
			KeyCombo combo = hotkey.getKeyCombo();
			if (combo.getKeys().containsAll(keybind.getKeys()) && keybind.getKeys().containsAll(combo.getKeys()) && hotkey != this) {
				hotkey.unregister();
			}
		}
		KeyListener.addListener(action);
		hotkeys.add(this);
	}
	
	public KeyCombo getKeyCombo() {
		return keyCombo;
	}
	
	public Script getScript() {
		return script;
	}
	
	public void unregister() {
		KeyListener.removeListener(action);
		script.kill();
		hotkeys.remove(this);
	}
	
}
