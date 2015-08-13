package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;

/** An event published when a script ends.
 * @author Andrew Keturi */
public class ScriptEndedEvent extends Event {
	public ScriptPageDefinition page;

	public ScriptEndedEvent(ScriptPageDefinition page) {
		this.page = page;
	}

}
