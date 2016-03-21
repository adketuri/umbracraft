package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/** An abstract window.
 * @author Andrew Keturi
 * @param <T> */
public abstract class Window<T extends WindowLayout> {
	private boolean escapeKeyCloses;
	public final T layout;

	public Window(T layout) {
		this.layout = layout;
	}

	void close(Listener listener) {
		layout.hide(listener);
	}

	/** called to dispose any resources */
	public void dispose() {

	}

	/** @return whether or not this window is touchable. default is
	 *         <code>true</code>. */
	public boolean isTouchable() {
		return true;
	}

	/** callback after the window has closed */
	public abstract void onClose();

	/** callback after the window has opened */
	public abstract void onOpen();

	void open() {
		layout.show();
		onOpen();
	}

	/** called to draw the layout */
	public void render() {
		layout.render();
	}

	/** Sets whether or not escape should close the window
	 * @param escapeKeyCloses */
	public void setEscapeKeyCloses(boolean escapeKeyCloses) {
		this.escapeKeyCloses = escapeKeyCloses;
	}

	/** called to update the layout */
	public void update() {
		layout.update();
		// check for close
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && escapeKeyCloses) {
			Game.windows().pop(this);
		}
	}
}
