package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.ObjectMap;

/** Defines behavior for players that reach the edge of maps.
 * @author Andrew Keturi */
public class TeleportDefinition extends Definition {

	/** The cardinal directions which players may teleport from
	 * @author Andrew Keturi */
	public static enum TeleportDirection {
		EAST, NORTH, SOUTH, WEST
	}

	/** A map of a direction to a particular game map {@link String} */
	public ObjectMap<String, String> adjacentMaps = new ObjectMap<>();

	@Override
	public String getName() {
		return "teleports";
	}

}
