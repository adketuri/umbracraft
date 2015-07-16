package net.alcuria.umbracraft.engine.scripts;

/** Any abstract command that is part of a scripted cutscene.
 * @author Andrew Keturi */
public abstract class ScriptCommand {

	public static enum CommandState {
		COMPLETE, NOT_STARTED, STARTED
	}

	private CommandState state = CommandState.NOT_STARTED;

	public void complete() {
		state = CommandState.COMPLETE;
		onCompleted();
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

	public abstract void onCompleted();

	public abstract void onStarted();

	public void setState(CommandState state) {
		state = this.state;

	}

	public final void start() {
		state = CommandState.STARTED;
		onStarted();
	}

	public abstract void update();

}
