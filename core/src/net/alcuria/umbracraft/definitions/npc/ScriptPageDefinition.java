package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
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

	public Array<ScriptCommand> commands;
	public Direction facing;
	public AnimationCollectionDefinition graphics;
	public boolean hidden;
	public Vector3 position;
	public Object precondition;
	public StartCondition start;

	@Override
	public String getName() {
		return "Page";
	}

}
