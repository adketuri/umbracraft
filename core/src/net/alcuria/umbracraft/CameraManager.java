package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.events.BaseEvent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.objects.GameObject;

import com.badlogic.gdx.graphics.OrthographicCamera;

/** A glorified wrapper for the {@link OrthographicCamera}
 * @author Andrew Keturi */
public class CameraManager implements EventListener {
	private final OrthographicCamera camera;
	private GameObject target;

	public CameraManager(OrthographicCamera camera) {
		this.camera = camera;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	@Override
	public void onEvent(BaseEvent event) {
		if (event instanceof CameraTargetEvent) {
			CameraTargetEvent camEvent = ((CameraTargetEvent) event);
			target = camEvent.gameObject;
			Game.log("Set camera target to " + target);
		}
	}

	public void update() {
		if (target != null) {
			if (Math.abs(camera.position.x - target.position.x) > 2) {
				if (camera.position.x < target.position.x) {
					camera.translate(2, 0);
					camera.update();
				} else if (camera.position.x > target.position.x) {
					camera.translate(-2, 0);
					camera.update();
				}
			}
			if (Math.abs(camera.position.y - target.position.y) > 2) {
				if (camera.position.y < target.position.y) {
					camera.translate(0, 2);
					camera.update();
				} else if (camera.position.y > target.position.y) {
					camera.translate(0, -2);
					camera.update();
				}
			}
		}
	}

}
