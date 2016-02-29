package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.windows.Window;

/** Published to notify the engine that a particular window is ready to be
 * removed.
 * @author Andrew Keturi */
public class WindowHideEvent extends Event {

	public Window<?> window;

	public WindowHideEvent(Window<?> window) {
		this.window = window;
	}

}
