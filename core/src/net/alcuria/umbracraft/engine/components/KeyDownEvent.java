package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.events.BaseEvent;

import com.badlogic.gdx.math.Vector3;

public class KeyDownEvent extends BaseEvent {

	public int keycode;
	public Vector3 source;

	public KeyDownEvent(int keycode, Vector3 source) {
		this.keycode = keycode;
		this.source = source;
	}

}
