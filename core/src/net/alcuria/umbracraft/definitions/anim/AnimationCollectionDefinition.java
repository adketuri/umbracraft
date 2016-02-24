package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** An AnimationCollection is a collection of references to
 * {@link AnimationGroupDefinition} classes for several animation types such as
 * running, walking, etc.
 * @author Andrew Keturi */
public class AnimationCollectionDefinition extends Definition {

	@Tooltip("The falling pose from AnimationGroupDefinition")
	public String falling;
	@Tooltip("The idle pose from AnimationGroupDefinition")
	public String idle;
	@Tooltip("The jumping pose from AnimationGroupDefinition")
	public String jumping;
	@Tooltip("The identifier for this definition")
	public String name;
	@Tooltip("The running pose from AnimationGroupDefinition")
	public String running;
	@Tooltip("A tag for sorting")
	public String tag;
	@Tooltip("The walking pose from AnimationGroupDefinition")
	public String walking;

	@Override
	public String getName() {
		return name != null ? name : "";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}

}
