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
	/** The frames for the animation. */
	public Array<AnimationFrameDefinition> frames;
	/** Height of a frame */
	public int height;
	/** An internal identifier */
	public int id;
	/** Whether or not to hold on last frame */
	public boolean keepLast;
	/** Whether or not it loops */
	public boolean loop;
	/** Width of a frame */
	public int width;
}
