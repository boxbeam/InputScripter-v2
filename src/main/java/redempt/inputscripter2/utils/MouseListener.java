package redempt.inputscripter2.utils;

import java.util.HashSet;
import java.util.Set;

import org.jnativehook.GlobalScreen;
import org.jnativehook.mouse.NativeMouseAdapter;
import org.jnativehook.mouse.NativeMouseEvent;

public class MouseListener {
	
	private static Set<Integer> pressed = new HashSet<>();
	
	public static void register() {
		GlobalScreen.addNativeMouseListener(new NativeMouseAdapter() {

			@Override
			public void nativeMousePressed(NativeMouseEvent event) {
				pressed.add(event.getButton());
			}

			@Override
			public void nativeMouseReleased(NativeMouseEvent event) {
				pressed.remove(event.getButton());
			}
		
		});
	}
	
	public static boolean isPressed(String mouseButton) {
		switch (mouseButton) {
			case "left":
				return pressed.contains(1);
			case "right":
				return pressed.contains(3);
			case "middle":
				return pressed.contains(2);
			default:
				return false;
		}
	}
	
}
