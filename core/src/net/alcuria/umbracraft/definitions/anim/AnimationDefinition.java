package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines an entire animation.
 * @author Andrew Keturi */
public class AnimationDefinition extends Definition {
	/** Number of game frames to delay before updating animation frame */
	public int delay;
	/** Full internal path to texture */
	public String filename;
	/** Height of a frame */
	public int frameHeight;
	/** The frames for the animation. */
	public Array<AnimationFrameDefinition> frames;
	/** Width of a frame */
	public int frameWidth;
	/** An internal identifier */
	@SuppressWarnings("unused")
	private int id;
	/** Whether or not to hold on last frame */
	public boolean keepLast;
	/** Whether or not it loops */
	public boolean loop;
	/** A friendly name */
	public String name;

	/** For serialization */
	public AnimationDefinition() {
	}

	/** Creates a module, setting the ID */
	public AnimationDefinition(int id) {
		this.id = id;
	}
}
