package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Listener;

/** An abstract window.
 * @author Andrew Keturi
 * @param <T> */
public abstract class Window<T extends WindowLayout> {
	public final T layout;

	public Window(T layout) {
		this.layout = layout;
	}

	void close(Listener listener) {
		layout.hide(listener);
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

	/** called to update the layout */
	public void update() {
		layout.update();
	}
}
