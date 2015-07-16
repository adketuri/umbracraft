package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.engine.windows.Window;

/** A {@link Window} that shows message boxes.
 * @author Andrew Keturi */
public class MessageWindow extends Window<MessageWindowLayout> {

	public MessageWindow(String message) {
		super(new MessageWindowLayout());
		layout.setMessage(message);
	}

}
