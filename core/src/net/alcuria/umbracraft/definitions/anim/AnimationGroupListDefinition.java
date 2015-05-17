package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

public class AnimationGroupListDefinition extends Definition {

	/** All the {@link AnimationGroupDefinition} definitions */
	public Array<AnimationGroupDefinition> animationGroups;
	/** The next identifier to use. */
	private int nextId;

	/** Adds an {@link AnimationGroupDefinition} */
	public void add() {
		if (animationGroups == null) {
			animationGroups = new Array<AnimationGroupDefinition>();
		}
		animationGroups.add(new AnimationGroupDefinition(nextId++));
	}

	/** Deletes an {@link AnimationGroupDefinition} */
	public void delete(AnimationGroupDefinition definition) {
		if (animationGroups == null) {
			return;
		}
		animationGroups.removeValue(definition, true);
	}
}
