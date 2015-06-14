package net.alcuria.umbracraft.engine.scripts;

public abstract class ScriptCommand {

	public static enum ScriptState {
		COMPLETE, NOT_STARTED, STARTED
	}

	private ScriptState state = ScriptState.NOT_STARTED;

	public void complete() {
		state = ScriptState.COMPLETE;
	}

	public boolean isDone() {
		return state == ScriptState.COMPLETE;
	}

	public void start() {
		state = ScriptState.STARTED;
	}

	public abstract void update();

}
