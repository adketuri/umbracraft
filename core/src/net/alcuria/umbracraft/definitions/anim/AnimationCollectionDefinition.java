package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** An AnimationCollection is a collection of references to
 * {@link AnimationGroupDefinition} classes for several animation types such as
 * running, walking, etc.
 * @author Andrew Keturi */
public class AnimationCollectionDefinition extends Definition {

	/** Falling pose */
	public String falling;
	/** Idle pose */
	public String idle;
	/** Jumping pose */
	public String jumping;
	/** Name */
	public String name;
	/** Running pose */
	public String running;
	/** Walking pose */
	public String walking;

	@Override
	public String getName() {
		return name != null ? name : "";
	}

}
