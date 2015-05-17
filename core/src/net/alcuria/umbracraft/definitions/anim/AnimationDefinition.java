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
	private int id;
	/** Whether or not to hold on last frame */
	public boolean keepLast;
	/** Whether or not the animation loops */
	public boolean loop;
	/** A friendly name */
	public String name;
	/** Width of a frame */
	public int width;

	/** For serialization */
	public AnimationDefinition() {
	}

	/** Creates a module, setting the ID */
	AnimationDefinition(int id) {
		this.id = id;
	}

	/** @return the unique id */
	public int getId() {
		return id;
	}
}
