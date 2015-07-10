package net.alcuria.umbracraft.engine.scripts;


public abstract class ScriptCommand {

	public static enum CommandState {
		COMPLETE, NOT_STARTED, STARTED
	}

	private CommandState state = CommandState.NOT_STARTED;

	public void complete() {
		state = CommandState.COMPLETE;
	}

	/** @return the {@link CommandState} */
	public CommandState getState() {
		return state;
	}

	/** @return true if the command has started */
	public boolean hasStarted() {
		return state == CommandState.STARTED;
	}

	/** @return true if the command has completed */
	public boolean isDone() {
		return state == CommandState.COMPLETE;
	}

	public void start() {
		state = CommandState.STARTED;
	}

	public abstract void update();

}
