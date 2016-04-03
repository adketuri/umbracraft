package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A glorified wrapper for the {@link OrthographicCamera}
 * @author Andrew Keturi */
public class View implements EventListener {
	private Rectangle bounds;
	private final OrthographicCamera camera;
	private float shakePeriod, shakeCounter, shakeFrequency, shakeAmplitude;
	private Entity target;
	private final OrthographicCamera uiCamera;
	private final Viewport viewport;
	private final Viewport worldViewport;

	public View() {
		camera = new OrthographicCamera(Config.viewWidth, Config.viewHeight);
		uiCamera = new OrthographicCamera(Config.viewWidth, Config.viewHeight);
		uiCamera.translate(Config.viewWidth / 2, Config.viewHeight / 2);
		uiCamera.update();
		viewport = new FitViewport(Config.viewWidth, Config.viewHeight);
		worldViewport = new FitViewport(Config.viewWidth, Config.viewHeight);
		worldViewport.setCamera(camera);
	}

	/** Immediately clears any bounds set by {@link View#setBounds(Rectangle)} */
	public void clearBounds() {
		bounds = null;
	}

	/** Immediately focuses the camera to its target */
	public void focus() {
		if (target == null) {
			return;
		}
		float dX = bounds != null ? (bounds.width > Config.viewWidth ? (target.position.x - camera.position.x) : bounds.x - camera.position.x) : 0;
		float dY = bounds != null ? (bounds.height > Config.viewHeight ? (target.position.y - camera.position.y) : bounds.y - camera.position.y) : 0;
		camera.translate(dX, dY);
		camera.update();
	}

	/** @return The boundaries of the camera. May be null if there are no
	 *         boundaries. */
	public Rectangle getBounds() {
		return bounds;
	}

	/** @return the camera for displaying entities, maps, etc */
	public OrthographicCamera getCamera() {
		return camera;
	}

	/** @return the ui camera for displaying ui elements */
	public OrthographicCamera getUiCamera() {
		return uiCamera;
	}

	/** @return the {@link Viewport} */
	public Viewport getViewport() {
		return viewport;
	}

	/** @return the {@link Viewport} of the world */
	public Viewport getWorldViewport() {
		return worldViewport;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof CameraTargetEvent) {
			CameraTargetEvent camEvent = ((CameraTargetEvent) event);
			target = camEvent.gameObject;
			Game.log("Set camera target to " + target.getName());
		}
	}

	/** Resizes the viewport
	 * @param width width of the viewport
	 * @param height height of the viewport */
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	/** Sets the boundaries of the camera. The camera will do its best to honor
	 * the boundaries set here; however, if the boundaries are too small the
	 * view will focus on the center of the region. When set, the camera will
	 * not translate beyond the rectangle's x/y from the bottom/left and x+w/y+h
	 * from the top right.
	 * @param bounds a {@link Rectangle} describing the boundaries of the image. */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
		if (bounds.width < Config.viewWidth) {
			bounds.x -= Math.abs(bounds.width - Config.viewWidth) / 2;
			bounds.width = Config.viewWidth;
		}
		if (bounds.height < Config.viewHeight) {
			bounds.y -= Math.abs(bounds.height - Config.viewHeight) / 2;
			bounds.height = Config.viewHeight;
		}
		this.bounds.x += Config.viewWidth / 2;
		this.bounds.y += Config.viewHeight / 2;
		update();
	}

	/** Sets a target for the camera to follow.
	 * @param target */
	public void setTarget(Entity target) {
		this.target = target;
	}

	public void shake(final float duration, final float frequency, final float amplitude) {
		shakePeriod = duration;
		shakeCounter = 0;
		shakeFrequency = frequency;
		shakeAmplitude = amplitude;
	}

	/** Updates the camera, moving towards a target if it exists and honoring any
	 * map boundaries if present. */
	public void update() {
		boolean moved = false;
		if (target != null) {
			float dX = bounds != null ? (bounds.width > Config.viewWidth ? (target.position.x - camera.position.x) / 20f : bounds.x - camera.position.x) : 0;
			float dY = bounds != null ? (bounds.height > Config.viewHeight ? (target.position.y - camera.position.y) / 20f : bounds.y - camera.position.y) : 0;
			if (dX < 0.5f && dX > -0.5f) {
				dX = 0;
			}
			if (dY < 0.5f && dY > -0.5f) {
				dY = 0;
			}
			camera.translate(dX, dY);
			moved = dX != 0 || dY != 0;
		}
		if (bounds != null) {
			if (bounds.width > Config.viewWidth) {
				if (camera.position.x < bounds.x) {
					camera.translate(bounds.x - camera.position.x, 0);
					moved = true;
				} else if (camera.position.x > bounds.width - Config.viewWidth / 2) {
					camera.translate(bounds.width - Config.viewWidth / 2 - camera.position.x, 0);
					moved = true;
				}
			}
			if (bounds.height > Config.viewHeight) {
				if (camera.position.y < bounds.y) {
					camera.translate(0, bounds.y - camera.position.y);
					moved = true;
				} else if (camera.position.y > bounds.height - Config.viewHeight / 2) {
					camera.translate(0, bounds.height - Config.viewHeight / 2 - camera.position.y);
					moved = true;
				}
			}
		}

		if (shakeCounter < shakePeriod) {
			shakeCounter += Gdx.graphics.getDeltaTime();
			if (shakeCounter >= shakePeriod) {
				shakeCounter = shakePeriod = 0;
			}
		}
		if (shakeCounter > 0) {
			float x = camera.position.x;
			float y = camera.position.y;
			final double motion = Math.sin(shakeCounter * shakeFrequency) * shakeAmplitude;
			camera.translate((int) motion, 0, 0);
			moved = true;
		}
		if (moved) {
			camera.update();
		}
	}

}
