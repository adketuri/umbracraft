package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

/** Defines a list of event commands to execute
 * @author Andrew Keturi */
public class ScriptPageDefinition extends Definition {

	public static enum ScriptTrigger {
		INSTANT, ON_INTERACTION, ON_TOUCH;
	}

	@Tooltip("An Animation to be added to this entity when this page is active")
	public String animation;
	@Tooltip("An AnimationCollection to be added to this entity when this page is active\nOverrides animation and animationGroup")
	public String animationCollection;
	@Tooltip("An Animation to be added to this entity when this page is active\nOverrides animation")
	public String animationGroup;
	@Tooltip("The starting command to execute")
	public ScriptCommand command;
	@Tooltip("Whether or not to halt player input upon activation")
	public boolean haltInput;
	@Tooltip("A name for the page")
	public String name;
	@Tooltip("The flag that when on will trigger this page")
	public String precondition;
	@Tooltip("How the script page starts execution")
	public ScriptTrigger trigger = ScriptTrigger.ON_TOUCH;

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

	/** Prints the commands recursively to stdout */
	public void printCommands() {
		command.print();
	}

}
