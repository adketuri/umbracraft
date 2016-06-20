package net.alcuria.umbracraft.definitions.map;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;

/** Defines a reference to an {@link EntityDefinition} on a map. Only one entity
 * per tile (not enforced).
 * @author Andrew Keturi */
public class EntityReferenceDefinition extends Definition {
	@Tooltip("The first argument (optional)")
	public String arg1;
	@Tooltip("The second argument (optional)")
	public String arg2;
	@Tooltip("The third argument (optional)")
	public String arg3;
	@Tooltip("The name of the entity to use")
	@Order(1)
	public String name;
	@Tooltip("The X coordinate")
	public int x;
	@Tooltip("The Y coordinate")
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
