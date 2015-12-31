package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to simply log some message to the console.
 * @author Andrew Keturi */
public class LogScriptCommand extends ScriptCommand {

	public String message = "";

	public LogScriptCommand() {
	}

	/** @param message the message to be displayed using {@link Game#log(String)} */
	public LogScriptCommand(final String message) {
		this.message = message;
	}

	@Override
	public String getName() {
		return "Log: '" + message + "'";
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return null;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {

	}

	@Override
	public void update() {
		Game.log("LogScriptCommand: " + message);
		complete();
	}

}
