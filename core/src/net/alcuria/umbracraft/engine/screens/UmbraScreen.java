package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.UmbraCraftApplication;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.manager.OverlayManager;
import net.alcuria.umbracraft.engine.windows.WindowStack;

import com.badlogic.gdx.Screen;

/** An extension of {@link Screen} to separate out update calls into a different
 * method. This screen handles tinting/overlays for transitions and anything
 * global to all implementing screens.
 * @author Andrew Keturi */
public abstract class UmbraScreen implements Screen {

	private OverlayManager overlays;

	@Override
	public void dispose() {
		if (overlays != null) {
			overlays.dispose();
		}
	}

	/** @return the {@link WindowStack} for the current screen. May be
	 *         <code>null</code> if the screen does not contain one. */
	public abstract WindowStack getWindows();

	/** Renders the screen. This should be used instead of the
	 * {@link Screen#render(float)} since that handles additional overlays and
	 * other global screen logic. */
	public abstract void onRender();

	/** A callback for handling any implementation-specific rendering.
	 * @param delta time since last frame, in seconds */
	public abstract void onUpdate(float delta);

	/** This should only be called from the {@link UmbraCraftApplication}. Any
	 * implementation-specific render logic belongs in
	 * {@link UmbraScreen#onRender() callback} */
	@Override
	public void render(float delta) {
		onRender();
		if (overlays != null) {
			overlays.render();
		} else if (Drawables.isInitialized()) {
			overlays = new OverlayManager();
		}
	}

	/** Updates the screen's overlays with a callback for any
	 * implementation-specific updates. This should only be called from the
	 * {@link UmbraCraftApplication}. Any implementation-specific update logic
	 * belongs in {@link UmbraScreen#onUpdate(float)} callback
	 * @param deltaTime the time since last update. */
	public void update(float deltaTime) {
		if (overlays != null) {
			overlays.update();
		}
		onUpdate(deltaTime);
	}

}
