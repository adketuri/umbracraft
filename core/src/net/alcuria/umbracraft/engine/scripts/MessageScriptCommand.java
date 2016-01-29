package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
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

	private final boolean dismissable = false;
	@Tooltip("The expression to display on the character's portrait")
	public MessageEmotion emotion = MessageEmotion.NEUTRAL;
	public String message = "";
	@Tooltip("The name to display with the message. Requires a speaker.")
	public String name = "";
	@Tooltip("The person speaking the message. Leave empty to omit.")
	public String speaker = "";

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
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Message: '" + StringUtils.truncate(message, 30) + "'";
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
		window = new MessageWindow(message, name, speaker, emotion);
		window.addCloseListener(close());
		Game.publisher().publish(new WindowShowEvent(window));
	}

	@Override
	public void update() {
		//		if (!Gdx.input.isKeyPressed(Keys.ENTER)) {
		//			dismissable = true;
		//		}
		//		if (dismissable && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
		//			complete();
		//		}
	}

}
