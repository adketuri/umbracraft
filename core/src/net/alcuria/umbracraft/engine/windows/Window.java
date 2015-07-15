package net.alcuria.umbracraft.engine.windows;

/** An abstract window.
 * @author Andrew Keturi
 * @param <T> */
public abstract class Window<T extends WindowLayout> {
	private final T layout;

	public Window(T layout) {
		this.layout = layout;
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
