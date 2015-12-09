package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.utils.Array;

/** Defines a list of event commands to execute
 * @author Andrew Keturi */
public class ScriptPageDefinition extends Definition {

	public static enum StartCondition {
		INSTANT, ON_INTERACTION, ON_TOUCH
	}

	/** The array of commands to execute */
	public Array<ScriptCommand> commands;
	/** Whether or not to halt player input on touch */
	public boolean haltInput;
	/** The precondition of this event page */
	public Object precondition;
	/** How the event starts */
	public StartCondition start;

	@Override
	public String getName() {
		return "Page";
	}

}
