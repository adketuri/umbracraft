package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.windows.Window;
import net.alcuria.umbracraft.engine.windows.WindowStack;

/** An event published to notify the {@link WindowStack} to display a new window.
 * @author Andrew Keturi */
public class WindowShowEvent extends Event {

	public Window window;

	public WindowShowEvent(Window window) {
		this.window = window;
	}
}
