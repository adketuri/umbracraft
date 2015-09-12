package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.utils.Array;

/** A simple event system. Listeners subscribe by calling
 * {@link EventPublisher#subscribe(EventListener)} and events are published via
 * the {@link EventPublisher#publish(Event)} method. Subscribing classes are
 * responsible for using instanceof to determine if any published events are the
 * correct type.
 * @author Andrew Keturi */
public class EventPublisher {
	Array<EventListener> listeners = new Array<EventListener>();

	/** Publishes an event, notifying all listeners.
	 * @param event the {@link Event} to publish. */
	public void publish(Event event) {
		for (EventListener listener : listeners) {
			listener.onEvent(event);
		}
	}

	/** Removes all listeners. */
	public void removeAllListeners() {
		listeners.clear();
		Game.log("Cleared.");
	}

	/** Adds an {@link EventListener} to the subscribed list to receive events
	 * when {@link EventPublisher#publish(Event)} is called.
	 * @param listener the {@link EventListener} */
	public void subscribe(EventListener listener) {
		listeners.add(listener);
		Game.log("Subscribed. Listener count: " + listeners.size);
	}

	/** Unsubscribes a single listener.
	 * @param listener */
	public void unsubscribe(EventListener listener) {
		listeners.removeValue(listener, true);
		Game.log("Unsubscribed. Listener count: " + listeners.size);
	}
}
