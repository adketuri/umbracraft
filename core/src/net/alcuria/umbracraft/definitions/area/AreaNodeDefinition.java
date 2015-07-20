package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** A single node on the {@link AreaDefinition}
 * @author Andrew */
public class AreaNodeDefinition extends Definition {

	public Array<AreaNodeDefinition> children;
	public String name;
	public Array<AreaNodeDefinition> parents;

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

}
