package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.events.Event;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

/** Published when a key is pressed.
 * @author Andrew Keturi */
public class KeyDownEvent extends Event {

	/** The keycode. See {@link Keys} */
	public int keycode;
	/** The location of the input press, in worldspace. */
	public Vector3 source;

	public KeyDownEvent(int keycode, Vector3 source) {
		this.keycode = keycode;
		this.source = source;
	}

}
