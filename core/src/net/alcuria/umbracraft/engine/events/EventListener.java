package net.alcuria.umbracraft.engine.events;

/** A basic listener interface for all events, containing a single method to be
 * called when the event is published.
 * @author Andrew Keturi */
public interface EventListener {

	public void onEvent(Event event);
}
