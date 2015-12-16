package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;

/** A {@link ScriptCommand} to simply log some message to the console.
 * @author Andrew Keturi */
public class LogScriptCommand extends ScriptCommand {

	public String message;

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
	public void onCompleted() {

	}

	@Override
	public void onStarted() {

	}

	@Override
	public void update() {
		Game.log("LogScriptCommand: " + message);
		complete();
	}

}
