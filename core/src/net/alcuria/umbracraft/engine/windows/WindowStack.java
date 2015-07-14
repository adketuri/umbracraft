package net.alcuria.umbracraft.engine.windows;

import java.util.ArrayList;

/** Contains a stack of {@link Window} classes with operations to add or remove
 * them. Windows display content (menus, messages, etc.) to the user.
 * @author Andrew Keturi */
public class WindowStack {

	private final ArrayList<Window<WindowLayout>> windows = new ArrayList<>();

	/** disposes all assets. etc used by the window stack */
	public void dispose() {

	}

	/** push a new window to the stack */
	public void push(Window<WindowLayout> window) {
		windows.add(window);
	}

}
