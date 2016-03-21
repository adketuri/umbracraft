package net.alcuria.umbracraft.engine.scripts;

import java.util.HashSet;
import java.util.Set;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Suggest;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A command to make operations on variables.
 * @author Andrew Keturi */
public class ControlVariableCommand extends ScriptCommand {

	/** The operations we may perform on a variable
	 * @author Andrew Keturi */
	public static enum ControlVariableOperation {
		OPT_0_ASSIGN("="), OPT_1_ADD("+"), OPT_2_SUBTRACT("-"), OPT_3_MULTIPLY("*"), OPT_4_DIVIDE("/"), OPT_5_MOD("%"), OPT_6_ABS("abs(x)");

		public String friendly;

		private ControlVariableOperation(String friendly) {
			this.friendly = friendly;
		}

		@Override
		public String toString() {
			return friendly;
		}
	}

	/** The methods of manipulating a variable
	 * @author Andrew Keturi */
	public static enum ControlVariableType {
		CONSTANT("Constant"), ENTITY_X_POS("Entity X Position"), ENTITY_Y_POS("Entity Y Position"), RANDOM("Random"), VARIABLE_VALUE("Variable");

		public String friendly;

		private ControlVariableType(String friendly) {
			this.friendly = friendly;
		}

		/** @return the fields which should be displayed for a particular
		 *         {@link ControlVariableType} */
		public String[] getFilter() {
			switch (this) {
			case CONSTANT:
				return new String[] { "id", "type", "operation", "constant" };
			case ENTITY_X_POS:
				return new String[] { "id", "type", "operation", "entityId", "self" };
			case ENTITY_Y_POS:
				return new String[] { "id", "type", "operation", "entityId", "self" };
			case RANDOM:
				return new String[] { "id", "type", "operation", "min", "max" };
			case VARIABLE_VALUE:
				return new String[] { "id", "type", "operation", "variableId" };
			}
			return new String[] { "id", "type" };
		}

		@Override
		public String toString() {
			return friendly;
		}

	}

	@Tooltip("A constant integer value")
	public int constant;
	@Tooltip("The id of the entity we're referencing")
	@Suggest("entities")
	public String entityId;
	@Tooltip("The id of the variable we want to change")
	@Order(0)
	public String id;
	@Tooltip("The minimum value (inclusive)")
	public int max;
	@Tooltip("The maximum value (inclusive)")
	public int min;
	@Tooltip("The operation to perform")
	@Order(1)
	public ControlVariableOperation operation = ControlVariableOperation.OPT_0_ASSIGN;
	@Tooltip("Use the id of the entity this script is attached to")
	public boolean self;
	@Tooltip("The method we're using to manipulate the variable")
	@Order(2)
	public ControlVariableType type;
	@Tooltip("The id of the variable we want to copy the value from")
	public String variableId;

	public ControlVariableCommand() {

	}

	@Override
	public ScriptCommand copy() {
		ControlVariableCommand cmd = new ControlVariableCommand();
		cmd.constant = constant;
		cmd.entityId = entityId;
		cmd.id = id;
		cmd.max = max;
		cmd.min = min;
		cmd.operation = operation;
		cmd.self = self;
		cmd.type = type;
		cmd.variableId = variableId;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		final HashSet<String> set = new HashSet<>();
		if (type == null) {
			type = ControlVariableType.CONSTANT;
		}
		final String[] filters = type.getFilter();
		for (String filter : filters) {
			set.add(filter);
		}
		return set;
	}

	@Override
	public String getName() {
		return "Control Variable: " + id + " " + operation.toString() + "  " + type.toString() + "(" + params() + ")";
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("entityId", Editor.db().entities().keys());
				put("variableId", Editor.db().variables().keys());
				put("id", Editor.db().variables().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		// get a reference to our variable
		int value = Game.variables().get(id);

		// determine the value of the operand we're using to change our value
		int operand = 0;
		switch (type) {
		case CONSTANT:
			operand = constant;
			break;
		case ENTITY_X_POS:
			final Entity e = self ? entity : Game.entities().find(entityId);
			if (e != null) {
				operand = (int) (e.position.x / Config.tileWidth);
			} else {
				Game.error("Entity not found on map: " + entityId);
			}
			break;
		case ENTITY_Y_POS:
			final Entity e2 = self ? entity : Game.entities().find(entityId);
			if (e2 != null) {
				operand = (int) (e2.position.y / Config.tileWidth);
			} else {
				Game.error("Entity not found on map: " + entityId);
			}
			break;
		case RANDOM:
			operand = MathUtils.random(min, max);
			break;
		case VARIABLE_VALUE:
			operand = Game.variables().get(variableId);
			break;
		}

		// determine how to manipulate our value
		switch (operation) {
		case OPT_1_ADD:
			value += operand;
			break;
		case OPT_2_SUBTRACT:
			value -= operand;
			break;
		case OPT_3_MULTIPLY:
			value *= operand;
			break;
		case OPT_4_DIVIDE:
			if (operand != 0) {
				value /= operand;
			} else {
				Game.error("Attempting to devide by zer-AAAAAAHHHH!!");
			}
			break;
		case OPT_5_MOD:
			if (operand != 0) {
				value %= operand;
			} else {
				Game.error("Attempting to devide by zer-AAAAAAHHHH!!");
			}
			break;
		case OPT_0_ASSIGN:
			value = operand;
			break;
		case OPT_6_ABS:
			value = Math.abs(operand);
			break;
		default:
			break;
		}

		// update our variable db
		Game.variables().set(id, value);
		complete();
	}

	private String params() {
		switch (type) {
		case CONSTANT:
			return String.valueOf(constant);
		case ENTITY_X_POS:
		case ENTITY_Y_POS:
			return self ? "<self>" : entityId;
		case RANDOM:
			return String.format("rand(%d, %d)", min, max);
		case VARIABLE_VALUE:
			return variableId;
		}
		return entityId;
	}

	@Override
	public void update() {
	}

}
