package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.manager.OverlayManager;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.graphics.Color;

/** An event to notify the {@link OverlayManager} when we want to tint the
 * screen/
 * @author Andrew Keturi */
public class TintScreenEvent extends Event {
	public Color color;
	public Listener listener;
	public float target, duration;

	public TintScreenEvent(Color color, float duration, Listener listener) {
		this.color = color;
		this.duration = duration;
		this.listener = listener;
	}

	/** @param target the target opacity of the screen tint.
	 * @param duration the duration of the change, in seconds.
	 * @param listener a {@link Listener} to invoke when tinting is complete.
	 *        May be null. */
	public TintScreenEvent(float target, float duration, Listener listener) {
		this.target = target;
		this.duration = duration;
		this.listener = listener;
	}

}
