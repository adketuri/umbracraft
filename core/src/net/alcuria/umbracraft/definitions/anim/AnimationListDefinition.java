package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines all of our {@link AnimationDefinition} classes.
 * @author Andrew Keturi */
public class AnimationListDefinition extends Definition {
	/** The {@link AnimationDefinition}s */
	public Array<AnimationDefinition> animations;
	/** The next identifier to use. */
	private int nextId;

	/** Adds an {@link AnimationDefinition} */
	public void add() {
		if (animations == null) {
			animations = new Array<AnimationDefinition>();
		}
		animations.add(new AnimationDefinition(nextId++));
	}

	/** Deletes an {@link AnimationDefinition} */
	public void delete(AnimationDefinition definition) {
		if (animations == null) {
			return;
		}
		animations.removeValue(definition, true);
	}

	@Override
	public String getName() {
		return "Animation List";
	}
}
