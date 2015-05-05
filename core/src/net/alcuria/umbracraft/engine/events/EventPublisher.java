package net.alcuria.umbracraft.engine.events;

import com.badlogic.gdx.utils.Array;

/** A simple event system. Listeners subscribe by calling
 * {@link EventPublisher#addListener(EventListener)} and events are published
 * via the {@link EventPublisher#publish(BaseEvent)} method. Subscribing classes
 * are responsible for using instanceof to determine if any published events are
 * the correct type.
 * @author Andrew Keturi */
public class EventPublisher {
	Array<EventListener> listeners = new Array<EventListener>();

	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	public void publish(BaseEvent event) {

		// Notify listeners
		for (EventListener listener : listeners) {
			listener.onEvent(event);
		}
	}

	public void removeAllListeners() {
		listeners.clear();
	}
}
