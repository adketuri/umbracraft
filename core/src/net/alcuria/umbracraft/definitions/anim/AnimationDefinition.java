package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines an entire animation.
 * @author Andrew Keturi */
public class AnimationDefinition extends Definition implements Comparable<AnimationDefinition> {

	/** Full internal path to texture */
	public String filename;
	/** The frames for the animation. */
	public Array<AnimationFrameDefinition> frames;
	/** Height of a frame */
	public int height;
	/** Whether or not to hold on last frame */
	public boolean keepLast;
	/** Whether or not the animation loops */
	public boolean loop;
	/** A friendly name */
	public String name;
	/** Origin x */
	public int originX;
	/** Origin y */
	public int originY;
	/** A tag for the animation to help with sorting */
	public String tag = "";
	/** Width of a frame */
	public int width;

	/** Uused for serialization */
	public AnimationDefinition() {
	}

	/** Creates an {@link AnimationDefinition} from an existing animation, by
	 * making a copy
	 * @param nextId
	 * @param definition */
	public AnimationDefinition(AnimationDefinition definition, int id) {
		filename = definition.filename;
		frames = new Array<AnimationFrameDefinition>();
		for (int i = 0; i < definition.frames.size; i++) {
			frames.add(definition.frames.get(i).copy());
		}
		height = definition.height;
		keepLast = definition.keepLast;
		loop = definition.loop;
		name = definition.name + " Copy " + id;
		width = definition.width;
		originX = definition.originX;
		originY = definition.originY;
		tag = definition.tag;
	}

	public AnimationDefinition(int id) {
		name = "Animation " + id;
		tag = "!New";
	}

	@Override
	public int compareTo(AnimationDefinition other) {
		if (tag.equals(other.tag)) {
			return name.compareTo(other.name);
		}
		return tag.compareTo(other.tag);
	}

	@Override
	public String getName() {
		return name;
	}
}
