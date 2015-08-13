package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;

/** An event published when a script starts
 * @author Andrew Keturi */
public class ScriptStartedEvent extends Event {
	public ScriptPageDefinition page;

	public ScriptStartedEvent(ScriptPageDefinition page) {
		this.page = page;
	}

}
