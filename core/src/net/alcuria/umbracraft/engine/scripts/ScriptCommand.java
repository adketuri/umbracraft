package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Any command that is part of a scripted cutscene. This is an extension of
 * {@link Definition} so public fields in implementing classes will be
 * serialized. Additionally, there are some implementation details in this
 * implementing ScriptCommands (for now).
 * @author Andrew Keturi */
public abstract class ScriptCommand extends Definition {

	/** The current state of the command as it is being executed.
	 * @author Andrew Keturi */
	public static enum CommandState {
		COMPLETE, NOT_STARTED, STARTED
	}

	private ScriptCommand next, parent;
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

	/** Called by the editor to get a filter on the commands to show */
	public abstract Set<String> getFilter();

	/** @return A human-readable name {@link String} */
	@Override
	public abstract String getName();

	/** @return the next command in the list */
	public ScriptCommand getNext() {
		return next;
	}

	/** Gets the parent command. May be <code>null</code>. */
	public ScriptCommand getParent() {
		return null;
	}

	/** @return the {@link CommandState} */
	public CommandState getState() {
		return state;
	}

	/** @return the suggestions hash, where the key is a field name eg, "id", and
	 *         the value is an array of suggestions. */
	public abstract ObjectMap<String, Array<String>> getSuggestions();

	@Override
	public String getTag() {
		return "";
	}

	/** Invoked after the command is completed */
	public abstract void onCompleted();

	/** Invoked when the command starts
	 * @param entity the {@link Entity} this command is attached to */
	public abstract void onStarted(Entity entity);

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

	/** Sets the parent node.
	 * @param parent the {@link ScriptCommand} parent. */
	public void setParent(ScriptCommand parent) {
		this.parent = parent;
	}

	/** Sets the state of the command, for example {@link CommandState#COMPLETE}
	 * when the command is done running.
	 * @param state the {@link CommandState} */
	public void setState(CommandState state) {
		this.state = state;
	}

	/** Called when the command is to be started. Has a callback for the
	 * subclasses ({@link ScriptCommand#onStarted()}) to do any
	 * subclass-specific work.
	 * @param entity */
	public final void start(Entity entity) {
		state = CommandState.STARTED;
		onStarted(entity);
	}

	/** Called every frame to update the {@link ScriptCommand} */
	public abstract void update();
}
