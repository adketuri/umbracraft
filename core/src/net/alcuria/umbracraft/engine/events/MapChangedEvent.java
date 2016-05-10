package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.util.O;

/** An event published when the map changes.
 * @author Andrew Keturi */
public class MapChangedEvent extends Event {
	/** The area of the new map. */
	public String area;
	/** The new node */
	public String node;
	/** the x and y coordinates */
	public int x, y;

	public MapChangedEvent(final String area, final String node, final int x, final int y) {
		this.area = O.notNull(area);
		this.node = O.notNull(node);
		this.x = x;
		this.y = y;
	}

}
