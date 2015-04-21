package net.alcuria.umbracraft.engine.events;

import com.badlogic.gdx.utils.Array;

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
}
