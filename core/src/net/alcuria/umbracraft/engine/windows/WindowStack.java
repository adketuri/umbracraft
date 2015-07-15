package net.alcuria.umbracraft.engine.windows;

import java.util.ArrayList;

/** Contains a stack of {@link Window} classes with operations to add or remove
 * them. Windows display content (menus, messages, etc.) to the user.
 * @author Andrew Keturi */
public class WindowStack {

	private final ArrayList<Window> windows = new ArrayList<>();

	/** disposes all assets. etc used by the window stack */
	public void dispose() {

	}

	/** push a new window to the stack */
	public void push(Window window) {
		windows.add(window);
	}

	/** renders the stack */
	public void render() {
		for (Window<WindowLayout> window : windows) {
			window.render();
		}
	}

	/** updates the stack */
	public void update() {
		for (Window<WindowLayout> window : windows) {
			window.update();
		}
	}

}
