package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.windows.InputCode;
import net.alcuria.umbracraft.engine.windows.message.MessageWindow;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.FileUtils;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
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

	public static enum MessageStyle {
		DARK(null, "ui/subtitle"), NORMAL("ui/bg", "ui/frame");
		public String bg;
		public String frame;

		private MessageStyle(String bg, String frame) {
			this.bg = bg;
			this.frame = frame;
		}
	}

	@Tooltip("If true, tapping will not advance the message window; it must auto-close via the duration field instead.")
	@Order(6)
	public boolean disableDismiss;
	@Tooltip("The duration after which the message auto-closes. 0 ignores.")
	@Order(5)
	public float duration = 0;
	@Tooltip("The expression to display on the character's portrait")
	@Order(4)
	public MessageEmotion emotion = MessageEmotion.NEUTRAL;
	@Tooltip("The filename id person speaking the message. Leave empty to omit.")
	@Order(3)
	public String faceId = "";
	@Tooltip("The message to display")
	@Order(1)
	public String message = "";
	@Order(2)
	@Tooltip("The name text to display with the message. Requires a speaker.")
	public String name = "";
	@Tooltip("Styling of the message box.")
	public MessageStyle style = MessageStyle.NORMAL;

	private float timer;
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
		cmd.disableDismiss = disableDismiss;
		cmd.duration = duration;
		cmd.timer = timer;
		cmd.style = style;
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
		return new ObjectMap<String, Array<String>>() {
			{
				put("faceId", FileUtils.getDirectoriesAt(Editor.db().config().projectPath + Editor.db().config().facePath));
			}
		};
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onStarted(Entity entity) {
		window = new MessageWindow(StringUtils.replaceArgs(message, entity.getArguments()), name, faceId, emotion, style);
		window.addCloseListener(close());
		Game.windows().push(window);
	}

	@Override
	public void update() {
		if (disableDismiss && duration > 0) {
			timer += Gdx.graphics.getDeltaTime();
			if (timer > duration) {
				window.invoke(InputCode.CONFIRM);
				complete();
			}
		}
	}
}
