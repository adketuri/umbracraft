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
public class View implements EventListener {
	private final OrthographicCamera camera;
	private Entity target;
	private final OrthographicCamera uiCamera;
	private final Viewport viewport;

	public View() {
		camera = new OrthographicCamera(Config.viewWidth, Config.viewHeight);
		uiCamera = new OrthographicCamera(Config.viewWidth, Config.viewHeight);
		uiCamera.translate(Config.viewWidth / 2, Config.viewHeight / 2);
		uiCamera.update();
		viewport = new FitViewport(Config.viewWidth, Config.viewHeight);
	}

	/** @return the camera for displaying entities, maps, etc */
	public OrthographicCamera getCamera() {
		return camera;
	}

	/** @return the ui camera for displaying ui elements */
	public OrthographicCamera getUiCamera() {
		return uiCamera;
	}

	@Override
	public void onEvent(BaseEvent event) {
		if (event instanceof CameraTargetEvent) {
			CameraTargetEvent camEvent = ((CameraTargetEvent) event);
			target = camEvent.gameObject;
			Game.log("Set camera target to " + target);
		}
	}

	/** Resizes the viewport
	 * @param width width of the viewport
	 * @param height height of the viewport */
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	/** Updates the camera */
	public void update() {
		if (target != null) {
			float dX = (target.position.x - camera.position.x) / 20f;
			float dY = (target.position.y - camera.position.y) / 20f;
			camera.translate(dX, dY);
			camera.update();
		}
	}

}
