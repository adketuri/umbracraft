package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

/** Defines a list of event commands to execute
 * @author Andrew Keturi */
public class ScriptPageDefinition extends Definition {

	public static enum StartCondition {
		INSTANT, ON_INTERACTION, ON_TOUCH
	}

	/** The commands to execute */
	public ScriptCommand command;
	/** Whether or not to halt player input on touch */
	public boolean haltInput;
	/** A name for the page */
	public String name;
	/** The precondition of this event page */
	public Object precondition;
	/** How the event starts */
	public StartCondition startCondition;

	//FIXME: temp
	public void addCommand(ScriptCommand command) {
		if (this.command == null) {
			this.command = command;
		} else {
			this.command.add(command);
		}
	}

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

	/** Gets the parent of a particular {@link ScriptCommand}
	 * @param start
	 * @param child
	 * @return */
	public ScriptCommand getParent(ScriptCommand start, ScriptCommand child) {
		if (start == null) {
			return null;
		} else if (start.getNext() == child) {
			Game.log("Found parent: " + start.getName());
			return start;
		} else {
			return getParent(start.getNext(), child);
		}
	}

	public void printCommands() {
		command.print();
	}

}
