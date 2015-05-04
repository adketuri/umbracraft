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

	public void delete(AnimationDefinition definition) {
		if (animations == null) {
			return;
		}
		animations.removeValue(definition, true);
	}

	public boolean hasId(int idx) {
		if (animations == null) {
			return false;
		}
		for (int i = 0; i < animations.size; i++) {
			if (animations.get(i).getId() == idx) {
				return true;
			}
		}
		return false;
	}
}
