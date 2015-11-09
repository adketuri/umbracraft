package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a collection of {@link AnimationDefinition} objects
 * @author Andrew Keturi */
@SuppressWarnings("unused")
public class AnimationGroupDefinition extends Definition {
	/** The id of the animation facing down */
	public String down;
	/** The id of the animation facing downleft */
	public String downLeft;
	/** The id of the animation facing downright */
	public String downRight;
	/** A unique identifier */
	private int id;
	/** The id of the animation facing left */
	public String left;
	/** A name */
	public String name;
	/** The id of the animation facing right */
	public String right;
	/** The id of the animation facing up */
	public String up;
	/** The id of the animation facing upLeft */
	public String upLeft;
	/** The id of the animation facing upRight */
	public String upRight;

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
