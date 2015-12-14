package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.StringUtils;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;
import net.alcuria.umbracraft.engine.windows.message.MessageWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

/** A {@link ScriptCommand} to display an ingame dialogue box. Commands resume
 * after the dialogue box has been dismissed.
 * @author Andrew Keturi */
public class MessageScriptCommand extends ScriptCommand {

	private boolean dismissable = false;
	private final String message;
	private MessageWindow window;

	public MessageScriptCommand(final String message) {
		this.message = message;
	}

	@Override
	public String getName() {
		return "Message: '" + StringUtils.truncate(message, 15) + "'";
	}

	@Override
	public void onCompleted() {
		Game.publisher().publish(new WindowHideEvent(window));
	}

	@Override
	public void onStarted() {
		window = new MessageWindow(message);
		Game.publisher().publish(new WindowShowEvent(window));
	}

	@Override
	public void update() {
		if (!Gdx.input.isKeyPressed(Keys.ENTER)) {
			dismissable = true;
		}
		if (dismissable && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
			complete();
		}
	}

}
