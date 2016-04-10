package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.graphics.Color;

/** Defines a single frame in an animation.
 * @author Andrew Keturi */
public class AnimationFrameDefinition extends Definition {
	@Tooltip("The color of the frame")
	@Order(500)
	public Color color;
	@Tooltip("The duration of the frame, in frames")
	public int duration;
	@Tooltip("Whether or not to mirror.")
	public boolean mirror;
	@Tooltip("An optional particle emitter to spawn at this frame")
	public String particle;
	@Tooltip("the X location on the grid")
	public int x;
	@Tooltip("the Y location on the grid")
	public int y;

	/** @return a copy of the frame. Sorta hacky but it works for now. */
	public AnimationFrameDefinition copy() {
		AnimationFrameDefinition def = new AnimationFrameDefinition();
		def.duration = duration;
		def.x = x;
		def.y = y;
		def.mirror = mirror;
		return def;
	}

	@Override
	public String getName() {
		return "AnimationFrame";
	}

	@Override
	public String getTag() {
		return "";
	}
}
