package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a collection of {@link AnimationDefinition} objects
 * @author Andrew Keturi */
public class AnimationGroupDefinition extends Definition {
	/** The total number of directions */
	public int directions;
	/** The id of the animation facing down */
	public String down;
	/** A unique identifier */
	private int id;
	/** A name */
	public String name;

	/** For serialization */
	public AnimationGroupDefinition() {

	}

	AnimationGroupDefinition(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}
}
