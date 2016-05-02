package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.scripts.BlockCommand;
import net.alcuria.umbracraft.engine.scripts.ConditionalCommand;
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

	/** Recursively gets the previous command of a particular
	 * {@link ScriptCommand}
	 * @param start the starting node, usually the root when invoked, but
	 * @param target the node we are searching for
	 * @return */
	public ScriptCommand getPrevious(ScriptCommand start, ScriptCommand target) {
		if (start == null) {
			return null;
		} else if (start.getNext() == target || (start instanceof BlockCommand && ((BlockCommand) start).block == target) || (start instanceof ConditionalCommand && ((ConditionalCommand) start).elseBlock == target)) {
			Game.log("Found parent: " + start.getName());
			return start;
		} else {
			ScriptCommand parent = null;
			if (start instanceof BlockCommand) {
				parent = getPrevious(((BlockCommand) start).block, target);
			}
			if (parent != null) {
				return parent;
			}
			return getPrevious(start.getNext(), target);
		}
	}

	@Override
	public String getTag() {
		return "";
	}

	/** Prints the commands recursively to stdout */
	public void printCommands() {
		command.print();
	}

}
