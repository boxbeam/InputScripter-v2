package redempt.inputscripter2.utils;

import java.util.ArrayList;
import java.util.List;

public class KeyCombo {
	
	private List<Integer> keys;
	
	public KeyCombo(List<Integer> keys) {
		this.keys = keys;
	}
	
	public List<Integer> getKeys() {
		return keys;
	}
	
	public boolean isPressed() {
		return KeyListener.keys.containsAll(keys);
	}
	
	public static KeyCombo fromString(String string) {
		List<Integer> keys = new ArrayList<>();
		String[] split = string.trim().split(" ");
		for (String key : split) {
			keys.add(Integer.parseInt(key));
		}
		return new KeyCombo(keys);
	}
	
	@Override
	public String toString() {
		String combine = "";
		for (int key : keys) {
			combine += key + " ";
		}
		combine = combine.trim();
		return combine;
	}
	
}
