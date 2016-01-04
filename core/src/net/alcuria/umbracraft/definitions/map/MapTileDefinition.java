package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single tile on the map.
 * @author Andrew Keturi */
public class MapTileDefinition extends Definition {

	/** The height of the map tile */
	public int altitude;
	/** For the editor, determines if we've already filled */
	public transient boolean filled;

	@Override
	public String getName() {
		return "Tile";
	}

}
