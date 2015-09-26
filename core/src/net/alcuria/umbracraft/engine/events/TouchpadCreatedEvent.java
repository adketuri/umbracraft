package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.components.DirectedInputComponent;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/** An event published when the touchpad is created so the
 * {@link DirectedInputComponent} knows about it.
 * @author Andrew Keturi */
public class TouchpadCreatedEvent extends Event {

	public Touchpad touchpad;

	public TouchpadCreatedEvent(Touchpad touchpad) {
		this.touchpad = touchpad;
	}
}
