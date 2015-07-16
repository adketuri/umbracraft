package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.windows.Window;

public class WindowHideEvent extends BaseEvent {

	public Window<?> window;

	public WindowHideEvent(Window<?> window) {
		this.window = window;
	}

}
