package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.definitions.Definition;

/** Any abstract command that is part of a scripted cutscene.
 * @author Andrew Keturi */
public abstract class ScriptCommand extends Definition {

	public static enum CommandState {
		COMPLETE, NOT_STARTED, STARTED
	}

	private ScriptCommand next;
	private CommandState state = CommandState.NOT_STARTED;

	/** Sets the next command, effectively adding a child command to the end of
	 * the list.
	 * @param command the {@link ScriptCommand} */
	public void add(ScriptCommand command) {
		if (next == null) {
			next = command;
		} else {
			next.add(command);
		}
	}

	/** Used internally by the script components to mark a command as complete.
	 * Any implementing class that requires custom work to be done when a
	 * command is complete should do so in the
	 * {@link ScriptCommand#onCompleted()} callback. */
	public final void complete() {
		state = CommandState.COMPLETE;
		onCompleted();
	}

	/** @return A human-readable name {@link String} */
	public abstract String getName();

	/** @return the next command in the list */
	public ScriptCommand getNext() {
		return next;
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

	/** Invoked after the command is completed */
	public abstract void onCompleted();

	/** Invoked when the command starts */
	public abstract void onStarted();

	/** Recursively prints all commands */
	public void print() {
		System.out.print(getName());
		if (next != null) {
			next.print();
		}
	}

	/** Sets the next command, wiping out any previous next.
	 * @param command the {@link ScriptCommand} */
	public void setNext(ScriptCommand command) {
		next = command;
	}

	/** Sets the state of the command, for example {@link CommandState#COMPLETE}
	 * when the command is done running.
	 * @param state the {@link CommandState} */
	public void setState(CommandState state) {
		this.state = state;
	}

	/** Called when the command is to be started. Has a callback for the
	 * subclasses ({@link ScriptCommand#onStarted()}) to do any
	 * subclass-specific work. */
	public final void start() {
		state = CommandState.STARTED;
		onStarted();
	}

	/** Called every frame to update the {@link ScriptCommand} */
	public abstract void update();

}
