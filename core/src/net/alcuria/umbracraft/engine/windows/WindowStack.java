package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.events.BaseEvent;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;

import com.badlogic.gdx.utils.Array;

/** Contains a stack of {@link Window} classes with operations to add or remove
 * them. Windows display content (menus, messages, etc.) to the user.
 * @author Andrew Keturi */
public class WindowStack implements EventListener {

	private final Array<Window<?>> windows = new Array<>();

	public WindowStack() {
		Game.publisher().subscribe(this);
	}

	/** disposes all assets. etc used by the window stack */
	public void dispose() {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(BaseEvent event) {
		if (event instanceof WindowShowEvent) {
			push(((WindowShowEvent) event).window);
		} else if (event instanceof WindowHideEvent) {
			pop(((WindowHideEvent) event).window);
		}
	}

	private void pop(Window<?> window) {
		if (window != null) {
			windows.removeValue(window, true);
		}
	}

	/** push a new window to the stack */
	public void push(Window<?> window) {
		if (window != null) {
			windows.add(window);
		}
	}

	/** renders the stack */
	public void render() {
		for (Window<?> window : windows) {
			window.render();
		}
	}

	/** updates the stack */
	public void update() {
		for (Window<?> window : windows) {
			window.update();
		}
	}

}
