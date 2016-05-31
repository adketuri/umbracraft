package net.alcuria.umbracraft.engine.windows.message;

import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand.MessageEmotion;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand.MessageStyle;
import net.alcuria.umbracraft.engine.windows.InputCode;
import net.alcuria.umbracraft.engine.windows.Window;
import net.alcuria.umbracraft.listeners.Listener;

/** A {@link Window} that shows message boxes.
 * @author Andrew Keturi */
public class MessageWindow extends Window<MessageWindowLayout> {
	private Listener close;
	private final MessageEmotion emotion;
	private final String message, faceId, name;
	private final MessageStyle style;

	public MessageWindow(String message, String name, String faceId, MessageEmotion emotion, MessageStyle style) {
		super(new MessageWindowLayout());
		this.message = message;
		this.name = name;
		this.faceId = faceId;
		this.style = style;
		this.emotion = emotion;
	}

	/** Adds a listener to invoke when the screen is closed */
	public void addCloseListener(Listener close) {
		this.close = close;
	}

	private void advance() {
		boolean complete = layout.advance();
		if (complete) {
			close();
		}
	}

	@Override
	public boolean isTouchable() {
		return false;
	}

	@Override
	public void onClose() {
		if (close != null) {
			close.invoke();
		}
	}

	@Override
	public void onKeyPressed(InputCode key) {
		if (key == InputCode.CONFIRM) {
			advance();
		}
	}

	@Override
	public void onOpen() {
		// start the message and allow touch input to advance the messages
		layout.setFace(faceId, emotion);
		layout.setName(name);
		layout.setMessage(message, false);
	}

}
