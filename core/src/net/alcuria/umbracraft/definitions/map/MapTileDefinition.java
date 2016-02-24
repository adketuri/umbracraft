package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single tile on the map.
 * @author Andrew Keturi */
public class MapTileDefinition extends Definition {

	/** The height of the map tile */
	public int altitude;
	/** For the editor, determines if we've already filled */
	public transient boolean filled;
	/** Overlays that may appear over tiles */
	public int overlayType;
	/** The type of tile, eg 0 for default, 1 for dirt, etc */
	public int type;

	@Override
	public String getName() {
		return "Tile";
	}

	@Override
	public String getTag() {
		return "";
	}

}
