package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.modules.EmptyCommand;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A block command to execute some commands if a given precondition is true.
 * @author Andrew Keturi */
public class ConditionalCommand extends BlockCommand {

	/** Methods of comparison for a {@link ConditionalCommand}
	 * @author Andrew Keturi */
	public static enum ConditionalComparison {
		OPT_0_EQU("=="), OPT_1_NEQ("!="), OPT_2_GT(">"), OPT_3_GTE(">="), OPT_4_LT("<"), OPT_5_LTE("<=");

		public String friendly;

		private ConditionalComparison(String friendly) {
			this.friendly = friendly;
		}

		@Override
		public String toString() {
			return friendly;
		}
	}

	private ScriptCommand calculatedNext;
	@Tooltip("The comparison operation")
	@Order(2)
	public ConditionalComparison comparison = ConditionalComparison.OPT_0_EQU;
	/** The else command */
	public ScriptCommand elseBlock = new EmptyCommand();
	@Tooltip("Add an else statement")
	@Order(4)
	public boolean includeElse;
	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(1)
	public String value1;

	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(3)
	public String value2;

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Conditional: %s %s %s", value1 != null ? value1 : "", comparison != null ? comparison : "", value2 != null ? value2 : "");
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("value1", new Array<String>() {
					{
						addAll(Editor.db().flags().keys());
						addAll(Editor.db().variables().keys());
					}
				});
				put("value2", new Array<String>() {
					{
						addAll(Editor.db().flags().keys());
						addAll(Editor.db().variables().keys());
					}
				});
			}
		};
	}

	private int getValue(String val) {
		return 0;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		boolean valid = false;
		switch (comparison) {
		case OPT_0_EQU:
			valid = getValue(value1) == getValue(value2);
			break;
		case OPT_1_NEQ:
			valid = getValue(value1) != getValue(value2);
			break;
		case OPT_2_GT:
			valid = getValue(value1) > getValue(value2);
			break;
		case OPT_3_GTE:
			valid = getValue(value1) >= getValue(value2);
			break;
		case OPT_4_LT:
			valid = getValue(value1) < getValue(value2);
			break;
		case OPT_5_LTE:
			valid = getValue(value1) <= getValue(value2);
			break;
		default:
			break;
		}

		// determine which command comes next (inside conditional, inside else, or after conditional)
		if (valid) {
			calculatedNext = getNext();
		} else if (includeElse) {
			calculatedNext = elseBlock;
		} else {
			calculatedNext = getParent().getNext();
		}

	}

	@Override
	public void update() {

	}

}
