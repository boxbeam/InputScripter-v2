package redempt.inputscripter2.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyAdapter;
import org.jnativehook.keyboard.NativeKeyEvent;

public class KeyListener {
	
	public static List<Integer> keys = new CopyOnWriteArrayList<>();
	private static List<Runnable> listeners = new CopyOnWriteArrayList<>();
	
	public static void register() throws NativeHookException {
		LogManager.getLogManager().reset();
		GlobalScreen.registerNativeHook();
		GlobalScreen.addNativeKeyListener(new NativeKeyAdapter() {

			@Override
			public void nativeKeyPressed(NativeKeyEvent e) {
				if (!keys.contains(e.getKeyCode())) {
					keys.add(e.getKeyCode());
				}
				for (Runnable runnable : listeners) {
					runnable.run();
				}
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent e) {
				keys.remove((Object) e.getKeyCode());
				for (Runnable runnable : listeners) {
					runnable.run();
				}
			}
			
		});
	}
	
	public static boolean isPressed(int key) {
		return keys.contains(key);
	}
	
	public static void addListener(Runnable runnable) {
		listeners.add(runnable);
	}
	
	public static void removeListener(Runnable runnable) {
		listeners.remove(runnable);
	}
	
	public static void clearListeners() {
		listeners.clear();
	}
	
	public List<Integer> getKeys() {
		return keys;
	}
	
}