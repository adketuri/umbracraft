package net.alcuria.umbracraft.definitions.anim;

import java.util.Comparator;

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

	public void clone(AnimationDefinition definition) {
		if (animations == null) {
			return;
		}
		animations.add(new AnimationDefinition(definition, nextId++));
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

	public void sort() {
		animations.sort(new Comparator<AnimationDefinition>() {

			@Override
			public int compare(AnimationDefinition o1, AnimationDefinition o2) {
				return o1.name.compareTo(o2.name);
			}
		});
	}

}
