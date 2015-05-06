package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single frame in an animation.
 * @author Andrew Keturi */
public class AnimationFrameDefinition extends Definition {
	/** The duration (in frames) of this frame */
	public int duration;
	/** The x position of the frame */
	public int x;
	/** The y position of the frame */
	public int y;

	/** @return a copy of the frame. Sorta hacky but it works for now. */
	public AnimationFrameDefinition copy() {
		AnimationFrameDefinition def = new AnimationFrameDefinition();
		def.duration = duration;
		def.x = x;
		def.y = y;
		return def;
	}
}
