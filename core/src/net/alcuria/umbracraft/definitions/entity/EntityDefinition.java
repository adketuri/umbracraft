package net.alcuria.umbracraft.definitions.entity;

import net.alcuria.umbracraft.definitions.component.ComponentDefinition;

import com.badlogic.gdx.utils.Array;

/** Defines an entity and its components.
 * @author Andrew Keturi */
public class EntityDefinition {
	/** the components */
	public Array<ComponentDefinition> components;
	/** The unique name of the entity */
	public String name;
}