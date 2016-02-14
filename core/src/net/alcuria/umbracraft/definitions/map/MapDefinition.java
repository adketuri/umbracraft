package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines a user-created map
 * @author Andrew Keturi */
public class MapDefinition extends Definition {
	/** Where on the map we enter from the four cardinal directions */
	public int eastX, eastY, northX, northY, southX, southY, westX, westY;
	/** The entities on this map */
	public Array<EntityReferenceDefinition> entities;
	/** The height of the map */
	private int height;
	/** The name of the map */
	public String name;
	/** The map tiles */
	public Array<Array<MapTileDefinition>> tiles;
	@Tooltip("The default level of the water, in tiles. Floating-point.")
	public float waterLevel = 1;
	/** The width of the map */
	private int width;

	/** Creates the tiles array */
	public void createTiles() {
		// create the new array
		tiles = new Array<Array<MapTileDefinition>>();
		for (int i = 0; i < width; i++) {
			tiles.insert(i, new Array<MapTileDefinition>());
			for (int j = 0; j < height; j++) {
				tiles.get(i).insert(j, new MapTileDefinition());
			}
		}
	}

	/** Finds an entity at the coordinates i,j. Will return <code>null</code> if
	 * the entity is not found.
	 * @param i
	 * @param j
	 * @return */
	public EntityReferenceDefinition findEntity(int i, int j) {
		if (entities == null) {
			entities = new Array<>();
		}
		for (EntityReferenceDefinition entity : entities) {
			if (entity.x == i && entity.y == j) {
				return entity;
			}
		}
		return null;
	}

	/** @return the map's height */
	public int getHeight() {
		return height;
	}

	@Override
	public String getName() {
		return name != null ? name : "Map";
	}

	/** Gets a tile definition at a particular x/y coordinate
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the {@link MapTileDefinition}, or <code>null</code> if it cannot
	 *         be found. */
	public MapTileDefinition getTileDefinition(int x, int y) {
		try {
			return tiles.get(x).get(y);
		} catch (Exception e) {
			return null;
		}
	}

	/** @return the map's width */
	public int getWidth() {
		return width;
	}

	/** Marks all tiles as not filled, after a completed fill. */
	public void resetFilled() {
		for (int i = 0; i < tiles.size; i++) {
			for (int j = 0; j < tiles.get(0).size; j++) {
				tiles.get(i).get(j).filled = false;
			}
		}

	}

	/** Resizes the map
	 * @param width the new width
	 * @param height the new height */
	public void resize(int width, int height) {
		int deltaWidth = width - this.width;
		int deltaHeight = height - this.height;
		setWidth(width);
		setHeight(height);
		// resize x
		while (deltaWidth != 0) {
			if (deltaWidth > 0) {
				// add width
				tiles.add(new Array<MapTileDefinition>());
				for (int j = 0; j < height; j++) {
					tiles.get(tiles.size - 1).insert(j, new MapTileDefinition());
				}
				deltaWidth--;
			} else {
				// remove width
				tiles.pop();
				deltaWidth++;
			}
		}
		// resize y
		while (deltaHeight != 0) {
			if (deltaHeight > 0) {
				// add height
				for (int j = 0; j < width; j++) {
					tiles.get(j).insert(0, new MapTileDefinition());
				}
				deltaHeight--;
			} else {
				// remove height
				for (int j = 0; j < width; j++) {
					tiles.get(j).removeIndex(0);
				}
				deltaHeight++;
			}
		}
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
