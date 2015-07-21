package net.alcuria.umbracraft.definitions.area;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** A single node on the {@link AreaDefinition}
 * @author Andrew Keturi */
public class AreaNodeDefinition extends Definition {

	public Array<AreaNodeDefinition> children;
	public int height;
	public int heightVariance;
	public String name;
	public Array<AreaNodeDefinition> parents;
	public int width;
	public int widthVariance;

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

}
