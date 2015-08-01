package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines a user-created map
 * @author Andrew Keturi */
public class MapDefinition extends Definition {

	/** the height of the map */
	private int height;
	/** the name of the map */
	public String name;
	/** the map tiles */
	public Array<Array<MapTileDefinition>> tiles;
	/** the width of the map */
	private int width;

	/** Creates the tiles array */
	public void createTiles() {
		// TODO: save off an old copy of the tiles (if available) to retain over
		tiles = new Array<Array<MapTileDefinition>>();
		for (int i = 0; i < width; i++) {
			tiles.insert(i, new Array<MapTileDefinition>());
			for (int j = 0; j < height; j++) {
				tiles.get(i).insert(j, new MapTileDefinition());
			}
		}
	}

	/** @return the map's height */
	public int getHeight() {
		return height;
	}

	@Override
	public String getName() {
		return name != null ? name : "Map";
	}

	/** @return the map's width */
	public int getWidth() {
		return width;
	}

	/** sets the map's height
	 * @param height the new height */
	public void setHeight(int height) {
		this.height = height;
	}

	/** sets the map's width
	 * @param width the new width */
	public void setWidth(int width) {
		this.width = width;
	}

}
