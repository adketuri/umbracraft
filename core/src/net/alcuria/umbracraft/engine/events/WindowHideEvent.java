package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.windows.Window;

public class WindowHideEvent extends Event {

	public Window<?> window;

	public WindowHideEvent(Window<?> window) {
		this.window = window;
	}

}
