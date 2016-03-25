package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;

/** An abstract window.
 * @author Andrew Keturi
 * @param <T> the layout for the window */
public abstract class Window<T extends WindowLayout> implements TypeListener<InputCode> {
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

	@Override
	public void invoke(InputCode type) {
		onKeyPressed(type);
	}

	/** @return whether or not this window is touchable. default is
	 *         <code>true</code>. */
	public boolean isTouchable() {
		return true;
	}

	/** callback after the window has closed */
	public abstract void onClose();

	/** callback after a key is pressed */
	public abstract void onKeyPressed(InputCode key);

	/** callback after the window has opened */
	public abstract void onOpen();

	void open() {
		layout.show();
		layout.setTypeListener(this);
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
