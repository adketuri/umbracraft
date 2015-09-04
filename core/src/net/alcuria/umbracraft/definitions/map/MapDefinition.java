package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;

import com.badlogic.gdx.utils.Array;

/** Defines a user-created map
 * @author Andrew Keturi */
public class MapDefinition extends Definition {
	/** The entities on this map */
	public Array<EntityReferenceDefinition> entities;
	/** The height of the map */
	private int height;
	/** The name of the map */
	public String name;
	/** The teleport locations */
	public TeleportDefinition teleport;
	/** The map tiles */
	public Array<Array<MapTileDefinition>> tiles;
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

	/** @return the map's width */
	public int getWidth() {
		return width;
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

	/** Sets a teleport node
	 * @param direction the {@link TeleportDirection}
	 * @param map a {@link String} representation of the map */
	public void setTeleport(TeleportDirection direction, String map) {
		if (teleport == null) {
			teleport = new TeleportDefinition();
		}
		teleport.adjacentMaps.put(direction, map);
	}

	/** sets the map's width
	 * @param width the new width */
	public void setWidth(int width) {
		this.width = width;
	}

}
