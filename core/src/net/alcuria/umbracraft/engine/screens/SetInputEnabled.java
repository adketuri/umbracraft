package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.engine.events.Event;

public class SetInputEnabled extends Event {

	public boolean enabled;

	public SetInputEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
