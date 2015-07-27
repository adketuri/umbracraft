package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a user-created map
 * @author Andrew Keturi */
public class MapDefinition extends Definition {

	/** the height of the map */
	public int height;
	/** the name of the map */
	public String name;
	/** the width of the map */
	public int width;

	@Override
	public String getName() {
		return name != null ? name : "Map";
	}

}
