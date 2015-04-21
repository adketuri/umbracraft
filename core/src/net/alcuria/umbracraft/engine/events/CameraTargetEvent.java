package net.alcuria.umbracraft.engine.events;

import net.alcuria.umbracraft.engine.objects.GameObject;

public class CameraTargetEvent extends BaseEvent {
	public GameObject player;

	public CameraTargetEvent(GameObject player) {
		this.player = player;
	}

}
