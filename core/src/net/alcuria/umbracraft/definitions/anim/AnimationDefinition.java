package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines an entire animation.
 * @author Andrew Keturi */
public class AnimationDefinition extends Definition {

	/** Full internal path to texture */
	public String filename;
	/** The frames for the animation. */
	public Array<AnimationFrameDefinition> frames;
	/** Height of a frame */
	public int height;
	/** An internal identifier */
	private final int id;
	/** Whether or not to hold on last frame */
	public boolean keepLast;
	/** Whether or not the animation loops */
	public boolean loop;
	/** A friendly name */
	public String name;
	/** Width of a frame */
	public int width;

	/** This should only be used for deserialization */
	public AnimationDefinition() {
		id = 0;
	}

	/** For serialization
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
		name = definition.name + " Copy";
		width = definition.width;
		this.id = id;
	}

	/** Creates a module, setting the ID */
	public AnimationDefinition(int id) {
		this.id = id;
	}

	/** @return the unique id */
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
}
