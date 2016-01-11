package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

/** Contains a stack of {@link Window} classes with operations to add or remove
 * them. Windows display content (menus, messages, etc.) to the user.
 * @author Andrew Keturi */
public class WindowStack implements EventListener {
	private boolean containsTouchable = false;
	private InputProcessor savedInputProcessor;
	private final Array<Window<?>> windows = new Array<>();

	public WindowStack() {
		Game.publisher().subscribe(this);
	}

	/** disposes all assets. etc used by the window stack */
	public void dispose() {
		for (Window<?> window : windows) {
			window.dispose();
		}
		Game.publisher().unsubscribe(this);
	}

	public InputProcessor getStage() {
		if (windows != null && windows.get(0) != null) {
			return windows.get(0).layout.stage;
		}
		throw new NullPointerException("Window not yet created. No stage for you.");
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof WindowShowEvent) {
			push(((WindowShowEvent) event).window);
		} else if (event instanceof WindowHideEvent) {
			pop(((WindowHideEvent) event).window);
		}
	}

	private void pop(final Window<?> window) {
		Game.log("popping " + window);
		if (window != null) {
			window.close(new Listener() {

				@Override
				public void invoke() {
					// remove window
					windows.removeValue(window, true);
					// check if we should reset touchable
					if (containsTouchable) {
						boolean isTouchable = false;
						for (Window<?> w : windows) {
							if (w.isTouchable()) {
								isTouchable = true;
								break;
							}
						}
						if (!isTouchable && savedInputProcessor != null) {
							containsTouchable = false;
							Gdx.input.setInputProcessor(savedInputProcessor);
							Game.log("Setting back to saved input processor");
						}
					}
				}
			});
		}
	}

	/** push a new window to the stack */
	public void push(Window<?> window) {
		if (window != null) {
			if (!containsTouchable && window.isTouchable()) {
				savedInputProcessor = Gdx.input.getInputProcessor();
				containsTouchable = true;
				Game.log("Saving off input processor");
			}
			Game.log("pushing " + window);
			windows.add(window);
			window.open();
		}

	}

	/** renders the stack */
	public void render() {
		for (Window<?> window : windows) {
			window.render();
		}
	}

	public int size() {
		return windows.size;
	}

	/** updates the stack */
	public void update() {
		for (Window<?> window : windows) {
			window.update();
		}
	}

}
