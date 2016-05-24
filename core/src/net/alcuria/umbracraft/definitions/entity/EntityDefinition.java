package net.alcuria.umbracraft.definitions.entity;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;

import com.badlogic.gdx.utils.Array;

/** Defines an entity and its components.
 * @author Andrew Keturi */
public class EntityDefinition extends Definition {
	/** the components */
	public Array<ComponentDefinition> components;
	@Tooltip("A unique name")
	public String name;
	@Tooltip("A rendering offset, in pixels. Determines render order.")
	public int renderOffset;
	@Tooltip("A modifier for how fast the entity travels")
	public float speedModifier = 1;
	@Tooltip("A tag for sorting")
	public String tag;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}