package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

/** Defines a list of event commands to execute
 * @author Andrew Keturi */
public class ScriptPageDefinition extends Definition {

	public static enum ScriptTrigger {
		INSTANT, ON_INTERACTION, ON_TOUCH;
	}

	/** The graphics for this script */
	public String animation;
	/** The graphics for this script */
	public String animationGroup;
	/** The commands to execute */
	public ScriptCommand command;
	/** Whether or not to halt player input on activation */
	public boolean haltInput;
	/** A name for the page */
	public String name;
	/** The flag that when on will trigger this page */
	public String precondition;
	/** How the event starts */
	public ScriptTrigger trigger = ScriptTrigger.ON_TOUCH;

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
