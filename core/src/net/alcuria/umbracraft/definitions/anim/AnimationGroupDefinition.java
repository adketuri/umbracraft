package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a collection of {@link AnimationDefinition} objects
 * @author Andrew Keturi */
public class AnimationGroupDefinition extends Definition {
	@Tooltip("If true, ignores diagonals")
	@Order(3)
	public boolean cardinalOnly;
	@Tooltip("The id of the animation facing down")
	public String down;
	@Tooltip("The id of the animation facing downleft")
	public String downLeft;
	@Tooltip("The id of the animation facing downright")
	public String downRight;
	@Tooltip("A unique identifier")
	private int id;
	@Tooltip("The id of the animation facing left")
	public String left;
	@Tooltip("A unique name")
	@Order(1)
	public String name;
	@Tooltip("The id of the animation facing right")
	public String right;
	@Tooltip("A tag for sorting")
	public String tag;
	@Tooltip("Optional path to an asset. When present, uses an rm2k-style template for the character instead of defining every animation")
	@Order(2)
	public String template;
	@Tooltip("The id of the animation facing up")
	public String up;
	@Tooltip("The id of the animation facing upLeft")
	public String upLeft;
	@Tooltip("The id of the animation facing upRight")
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

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
