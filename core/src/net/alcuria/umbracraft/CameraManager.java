package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.BaseEvent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.events.EventListener;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A glorified wrapper for the {@link OrthographicCamera}
 * @author Andrew Keturi */
public class CameraManager implements EventListener {
	private final OrthographicCamera camera;
	private Entity target;
	private final Viewport viewport;

	public CameraManager(OrthographicCamera camera) {
		this.camera = camera;
		viewport = new FitViewport(Config.viewWidth, Config.viewHeight);
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

	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	public void update() {
		if (target != null) {

			float dX = (target.position.x - camera.position.x) / 20f;
			float dY = (target.position.y - camera.position.y) / 20f;
			camera.translate(dX, dY);
			camera.update();
		}
	}

}
