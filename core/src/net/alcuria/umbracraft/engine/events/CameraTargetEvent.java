package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.objects.GameObject;

/** An event published when we want to target the camera at some
 * {@link GameObject}
 * @author Andrew */
public class CameraTargetEvent extends BaseEvent {
	public GameObject gameObject;

	public CameraTargetEvent(GameObject gameObject) {
		this.gameObject = gameObject;
	}

}
