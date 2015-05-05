package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.entities.Entity;

/** An event published when we want to target the camera at some
 * {@link Entity}
 * @author Andrew */
public class CameraTargetEvent extends BaseEvent {
	public Entity gameObject;

	public CameraTargetEvent(Entity gameObject) {
		this.gameObject = gameObject;
	}

}
