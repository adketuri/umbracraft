package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.definitions.Definition;

/** An Area consists of several nodes, which eventually become maps the player
 * can explore. Nodes are either defined as a pre-existing map or randomly
 * generated with developer-defined constraints.
 * @author Andrew Keturi */
public class AreaDefinition extends Definition {

	/** the name of the area */
	public String name;
	/** the root node */
	public AreaNodeDefinition root;

	@Override
	public String getName() {
		return name != null ? name : "";
	}

}
