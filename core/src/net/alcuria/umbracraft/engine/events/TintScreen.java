package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.manager.OverlayManager;
import net.alcuria.umbracraft.listeners.Listener;

/** An event to notify the {@link OverlayManager} when we want to tint the
 * screen/
 * @author Andrew Keturi */
public class TintScreen extends Event {
	public Listener listener;
	public float target, duration;

	/** @param target the target opacity of the screen tint.
	 * @param duration the duration of the change, in seconds.
	 * @param listener a {@link Listener} to invoke when tinting is complete.
	 *        May be null. */
	public TintScreen(float target, float duration, Listener listener) {
		this.target = target;
		this.duration = duration;
		this.listener = listener;
	}

}
