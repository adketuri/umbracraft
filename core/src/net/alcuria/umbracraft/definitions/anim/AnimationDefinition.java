package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines an entire animation.
 * @author Andrew Keturi */
public class AnimationDefinition extends Definition implements Comparable<AnimationDefinition> {

	@Tooltip("Filename, inside to the internal /sprites/animations/\n folder, to the asset.")
	@Order(3)
	public String filename;
	@Tooltip("The frames for the animation")
	public Array<AnimationFrameDefinition> frames;
	@Tooltip("The height of a single frame")
	@Order(5)
	public int height;
	@Tooltip("Whether or not to hold on last frame")
	public boolean keepLast;
	@Tooltip("Whether or not the animation loops")
	public boolean loop;
	@Tooltip("A friendly name")
	@Order(1)
	public String name;
	@Tooltip("The x origin of a frame")
	@Order(6)
	public int originX;
	@Tooltip("The x origin of a frame")
	@Order(7)
	public int originY;
	@Tooltip("A tag for the animation to help with sorting")
	@Order(2)
	public String tag = "";
	@Tooltip("The width of a frame")
	@Order(4)
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
