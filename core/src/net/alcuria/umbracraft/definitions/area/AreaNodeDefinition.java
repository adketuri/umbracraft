package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;

import com.badlogic.gdx.utils.Array;

/** A single node on the {@link AreaDefinition}.
 * @author Andrew Keturi */
public class AreaNodeDefinition extends Definition {
	/** all children nodes for the area */
	public Array<AreaNodeDefinition> children;
	@Tooltip("The map definition to use at this node")
	public String mapDefinition;
	@Tooltip("A name given to this node")
	public String name;
	@Tooltip("The teleports")
	public TeleportDefinition teleport;

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
