package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** A single node on the {@link AreaDefinition}.
 * @author Andrew Keturi */
public class AreaNodeDefinition extends Definition {

	/** all children nodes for the area */
	public Array<AreaNodeDefinition> children;
	/** the name of the area map */
	public String name;

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

}
