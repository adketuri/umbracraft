package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;

/** An event that gets triggered when a flag has changed.
 * @author Andrew Keturi */
public class FlagChangedEvent extends Event {
	/** The ID of the flag that changed */
	public String id;
	/** The entity that triggered the change */
	public Entity source;

	public FlagChangedEvent(String id, Entity source) {
		this.id = id;
		this.source = source;
	}
}
