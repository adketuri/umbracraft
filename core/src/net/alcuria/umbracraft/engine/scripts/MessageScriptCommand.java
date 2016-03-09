package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;
import net.alcuria.umbracraft.engine.windows.message.MessageWindow;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to display an ingame dialogue box. Commands resume
 * after the dialogue box has been dismissed.
 * @author Andrew Keturi */
public class MessageScriptCommand extends ScriptCommand {

	/** Various facial expressions
	 * @author Andrew Keturi */
	public static enum MessageEmotion {
		ANGRY, HAPPY, NEUTRAL, SAD
	}

	@Tooltip("The expression to display on the character's portrait")
	@Order(4)
	public MessageEmotion emotion = MessageEmotion.NEUTRAL;
	@Tooltip("The filename id person speaking the message. Leave empty to omit a face from displaying.")
	@Order(3)
	public String faceId = "";
	@Tooltip("The message to display")
	@Order(1)
	public String message = "";
	@Order(2)
	@Tooltip("The name text to display with the message. Requires a speaker.")
	public String name = "";

	private MessageWindow window;

	public MessageScriptCommand() {
	}

	public MessageScriptCommand(final String message) {
		this.message = message;
	}

	public Listener close() {
		return new Listener() {

			@Override
			public void invoke() {
				complete();
			}
		};
	}

	@Override
	public ScriptCommand copy() {
		MessageScriptCommand cmd = new MessageScriptCommand();
		cmd.emotion = emotion;
		cmd.faceId = faceId;
		cmd.message = message;
		cmd.name = name;
		cmd.window = window;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Message: '" + StringUtils.truncate(message, 60) + "'";
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return null;
	}

	@Override
	public void onCompleted() {
		Game.publisher().publish(new WindowHideEvent(window));
	}

	@Override
	public void onStarted(Entity entity) {
		window = new MessageWindow(message, name, faceId, emotion);
		window.addCloseListener(close());
		Game.publisher().publish(new WindowShowEvent(window));
	}

	@Override
	public void update() {
	}

}
