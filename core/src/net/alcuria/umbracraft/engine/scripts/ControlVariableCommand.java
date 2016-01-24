package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A command to make operations on variables.
 * @author Andrew Keturi */
public class ControlVariableCommand extends ScriptCommand {

	public static enum ControlVariableOperation {
		ADD, ASSIGN, DIVIDE, MOD, MULTIPLY, SUBTRACT
	}

	public static enum ControlVariableType {
		CONSTANT, ENTITY_X_POS, ENTITY_Y_POS, VARIABLE_VALUE,
	}

	public int constant;
	public String entityId;
	@Tooltip("The variable's id")
	public String id;
	public String variableId;

	@Override
	public String getName() {
		return "Control Variable: " + id;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return null;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {

	}

	@Override
	public void update() {

	}

}
