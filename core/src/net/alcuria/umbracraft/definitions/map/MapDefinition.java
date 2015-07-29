package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a user-created map
 * @author Andrew Keturi */
public class MapDefinition extends Definition {

	/** the height of the map */
	public int height;
	/** the name of the map */
	public String name;
	/** the map tiles */
	public MapTileDefinition[][] tiles;
	/** the width of the map */
	public int width;

	/** Creates the tiles array */
	public void createTiles() {
		// TODO: save off an old copy of the tiles (if available) to retain over
		tiles = new MapTileDefinition[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new MapTileDefinition();
			}
		}
	}

	@Override
	public String getName() {
		return name != null ? name : "Map";
	}

}
