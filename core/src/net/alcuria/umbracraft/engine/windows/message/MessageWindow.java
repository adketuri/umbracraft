package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.KeyDownEvent;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.windows.Window;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.Input.Keys;

/** A {@link Window} that shows message boxes.
 * @author Andrew Keturi */
public class MessageWindow extends Window<MessageWindowLayout> implements EventListener {
	private Listener close;
	private final String message;

	public MessageWindow(String message) {
		super(new MessageWindowLayout());
		this.message = message;
		Game.publisher().subscribe(this);
	}

	/** Adds a listener to invoke when the screen is closed */
	public void addCloseListener(Listener close) {
		this.close = close;
	}

	private void advance() {
		boolean complete = layout.advance();
		if (complete) {
			Game.windows().pop(this);
		}
	}

	@Override
	public boolean isTouchable() {
		return false;
	}

	@Override
	public void onClose() {
		Game.publisher().unsubscribe(this);
		if (close != null) {
			close.invoke();
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof KeyDownEvent) {
			if (((KeyDownEvent) event).keycode == Keys.ENTER) {
				advance();
			}
		}
	}

	@Override
	public void onOpen() {
		layout.setMessage(message);
	}

}
