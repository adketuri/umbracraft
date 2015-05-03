package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines all of our {@link AnimationDefinition} classes.
 * @author Andrew Keturi */
public class AnimationListDefinition extends Definition {
	/** The {@link AnimationDefinition}s */
	public Array<AnimationDefinition> animations;

	/** Adds an {@link AnimationDefinition} */
	public void add() {
		if (animations == null) {
			animations = new Array<AnimationDefinition>();
		}
		animations.add(new AnimationDefinition(animations.size));
	}
}
