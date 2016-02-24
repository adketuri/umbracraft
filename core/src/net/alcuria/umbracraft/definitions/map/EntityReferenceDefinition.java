package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;

/** Defines a reference to an {@link EntityDefinition} on a map. Only one entity
 * per tile (not enforced).
 * @author Andrew Keturi */
public class EntityReferenceDefinition extends Definition {
	/** the name of the entity to use */
	public String name;
	/** the x coordinate */
	public int x;
	/** the y coordinate */
	public int y;

	@Override
	public String getName() {
		return name + "@" + x + "," + y;
	}

	@Override
	public String getTag() {
		return "";
	}

}
