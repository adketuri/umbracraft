package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a collection of {@link AnimationDefinition} objects
 * @author Andrew Keturi */
public class AnimationGroupDefinition extends Definition {
	/** The total number of directions */
	public int directions;
	/** The down definition */
	public AnimationDefinition down;
}
