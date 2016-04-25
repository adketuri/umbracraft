package net.alcuria.umbracraft.save.model;

import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;

import com.badlogic.gdx.math.Vector3;

/** Describes the location of a character for saving and loading
 * @author Andrew Keturi */
public class Location {
	/** The {@link AreaDefinition} id */
	public String area;
	/** The {@link MapDefinition} id */
	public String map;
	/** The {@link AreaDefinition} node */
	public String node;
	/** The player's coordinates */
	public Vector3 position;

	/** For deserialization */
	public Location() {
	}

	public Location(String area, String node, String map, Vector3 position) {
		this.area = area;
		this.node = node;
		this.map = map;
		this.position = position;
	}
}
