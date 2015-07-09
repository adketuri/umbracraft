package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** Defines a list of event commands to execute
 * @author Andrew Keturi */
public class ScriptPageDefinition extends Definition {

	public static enum StartCondition {
		INSTANT, ON_INTERACTION, ON_TOUCH
	}

	/** The array of commands to execute */
	public Array<ScriptCommand> commands;
	/** The facing direction of the event command */
	public Direction facing;
	/** Whether or not to halt player input on touch */
	public boolean haltInput;
	/** Whether or not this event is hidden */
	public boolean hidden;
	/** The position of the event on this page */
	public Vector3 position;
	/** The precondition of this event page */
	public Object precondition;
	/** How the event starts */
	public StartCondition start;

	@Override
	public String getName() {
		return "Page";
	}

}
