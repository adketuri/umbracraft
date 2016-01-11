package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.engine.windows.Window;

/** A {@link Window} that shows message boxes.
 * @author Andrew Keturi */
public class MessageWindow extends Window<MessageWindowLayout> {
	private final String message;

	public MessageWindow(String message) {
		super(new MessageWindowLayout());
		this.message = message;
	}

	@Override
	public boolean isTouchable() {
		return false;
	}

	@Override
	public void onClose() {

	}

	@Override
	public void onOpen() {
		layout.setMessage(message);
	}

}
