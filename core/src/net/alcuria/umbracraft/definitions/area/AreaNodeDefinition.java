package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;

import com.badlogic.gdx.utils.Array;

/** A single node on the {@link AreaDefinition}.
 * @author Andrew Keturi */
public class AreaNodeDefinition extends Definition {
	/** all children nodes for the area */
	public Array<AreaNodeDefinition> children;
	/** the area map's height */
	public int height;
	/** the area map's variance */
	public int heightVariance;
	/** the map definition to use */
	public String mapDefinition;
	/** the name of the area map */
	public String name;
	/** The teleport locations */
	public TeleportDefinition teleport;
	/** the area map's width */
	public int width;
	/** the aream map's variance (width) */
	public int widthVariance;

	public AreaNodeDefinition() {
		teleport = new TeleportDefinition();
	}

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

	/** Sets a teleport node
	 * @param direction the {@link TeleportDirection}
	 * @param map a {@link String} representation of the map */
	public void setTeleport(TeleportDirection direction, String map) {
		if (teleport == null) {
			teleport = new TeleportDefinition();
		}
		teleport.adjacentMaps.put(direction.toString(), map);
	}

}
