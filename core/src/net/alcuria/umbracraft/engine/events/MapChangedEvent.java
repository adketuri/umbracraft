package net.alcuria.umbracraft.engine.events;

/** An event published when the map changes.
 * @author Andrew Keturi */
public class MapChangedEvent extends Event {
	/** The ID of the new map. */
	public String id;

	public MapChangedEvent(final String id) {
		this.id = id;
	}

}
