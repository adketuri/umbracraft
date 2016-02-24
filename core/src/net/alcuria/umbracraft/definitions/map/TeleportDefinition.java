package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.ObjectMap;

/** Defines behavior for players that reach the edge of maps.
 * @author Andrew Keturi */
public class TeleportDefinition extends Definition {

	/** The cardinal directions which players may teleport from
	 * @author Andrew Keturi */
	public static enum TeleportDirection {
		EAST, NORTH, SOUTH, WEST;

		/** @return the opposite {@link TeleportDirection} */
		public TeleportDirection opposite() {
			switch (this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
			}
			throw (new IllegalArgumentException("No opposite for " + this));
		}
	}

	/** A map of a direction to a particular game map {@link String} */
	public ObjectMap<String, String> adjacentMaps = new ObjectMap<>();

	@Override
	public String getName() {
		return "teleports";
	}

	@Override
	public String getTag() {
		return null;
	}

}
