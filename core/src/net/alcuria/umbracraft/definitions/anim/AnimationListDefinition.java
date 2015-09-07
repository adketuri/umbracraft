package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.ObjectMap;

/** Defines all of our {@link AnimationDefinition} classes.
 * @author Andrew Keturi */
public class AnimationListDefinition extends Definition {
	/** The {@link AnimationDefinition}s */
	public ObjectMap<String, AnimationDefinition> animations;
	/** The next identifier to use. */
	private int nextId;

	/** Adds an {@link AnimationDefinition} */
	public void add() {
		if (animations == null) {
			animations = new ObjectMap<>();
		}
		final AnimationDefinition animationDefinition = new AnimationDefinition(nextId++);
		animations.put(animationDefinition.name, animationDefinition);
	}

	public void clone(AnimationDefinition definition) {
		if (animations == null) {
			return;
		}
		final AnimationDefinition animationDefinition = new AnimationDefinition(definition, nextId++);
		animations.put(animationDefinition.name, animationDefinition);
	}

	/** Deletes an {@link AnimationDefinition} */
	public void delete(AnimationDefinition definition) {
		if (animations == null) {
			return;
		}
		animations.remove(definition.name);
	}

	@Override
	public String getName() {
		return "Animation List";
	}
}
