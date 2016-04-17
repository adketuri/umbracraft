package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A block command to allow simple script control flow based based on some
 * preconditions. This cannot parse any arbitrary logical expression. It is
 * instead limited to only testing up to two separate conditions.
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

	/** Represents a logical operator in a conditional command
	 * @author Andrew Keturi */
	public static enum LogicalOperator {
		OPT_0_OR("||"), OPT_1_AND("&&");

		public String friendly;

		private LogicalOperator(String friendly) {
			this.friendly = friendly;
		}

		@Override
		public String toString() {
			return friendly;
		}
	}

	private transient ScriptCommand calculatedNext;
	@Tooltip("The comparison operator")
	@Order(2)
	public ConditionalComparison comparison = ConditionalComparison.OPT_0_EQU;
	@Tooltip("The second comparison operator")
	@Order(6)
	public ConditionalComparison comparison2 = ConditionalComparison.OPT_0_EQU;
	/** The else command */
	public ScriptCommand elseBlock = new EmptyCommand();
	@Tooltip("Add an else statement")
	@Order(8)
	public boolean includeElse;
	private boolean isNested;
	@Order(4)
	public LogicalOperator logicalOperator = LogicalOperator.OPT_0_OR;
	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(1)
	public String value1 = "";
	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(3)
	public String value2 = "";
	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(5)
	public String value3 = "";
	@Tooltip("The comparison value, either a variable/flag or a constant")
	@Order(7)
	public String value4 = "";

	@Override
	public ScriptCommand copy() {
		ConditionalCommand cmd = new ConditionalCommand();
		cmd.comparison = comparison;
		cmd.comparison2 = comparison2;
		cmd.elseBlock = elseBlock;
		cmd.includeElse = includeElse;
		cmd.logicalOperator = logicalOperator;
		cmd.value1 = value1;
		cmd.value2 = value2;
		cmd.value3 = value3;
		cmd.value4 = value4;
		return cmd;
	}

	/** @return the next {@link ScriptCommand} instruction */
	public ScriptCommand getCalculated() {
		return calculatedNext;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		if (StringUtils.isNotEmpty(value3) && StringUtils.isNotEmpty(value4)) {
			return String.format("Conditional: %s %s %s %s %s %s %s", value1, comparison, value2, logicalOperator, value3, comparison2, value4);
		}
		return String.format("Conditional: %s %s %s", value1, comparison, value2);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				for (int i = 1; i <= 4; i++) {
					put("value" + i, new Array<String>() {
						{
							addAll(Editor.db().flags().keys());
							addAll(Editor.db().variables().keys());
						}
					});
				}
			}
		};
	}

	/** determine the value of a particular string. If it's an integer we treat
	 * it as a number, but if it's a String we need to dig into the
	 * variable/flag managers. */
	private int getValue(String val) {
		if (StringUtils.isNumber(val)) {
			return Integer.parseInt(val);
		} else if (Game.variables().exists(val)) {
			return Game.variables().get(val);
		} else if (Game.flags().isSet(val)) {
			return 1;
		} else if (StringUtils.isNotEmpty(val) && val.equalsIgnoreCase("true")) {
			return 1;
		}
		return 0;
	}

	/** @return <code>true</code> if the next command is nested */
	public boolean isNextNested() {
		return isNested;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		// 1. test the condition
		boolean valid = false;
		if (StringUtils.isNotEmpty(value3) && StringUtils.isNotEmpty(value4)) {
			// if third and fourth values are present, use logical operator to test condition
			if (logicalOperator == LogicalOperator.OPT_0_OR) {
				valid = testCondition(value1, value2, comparison) || testCondition(value3, value4, comparison2);
			} else if (logicalOperator == LogicalOperator.OPT_1_AND) {
				valid = testCondition(value1, value2, comparison) && testCondition(value3, value4, comparison2);
			}
		} else {
			// just use the first two values to test the condition
			valid = testCondition(value1, value2, comparison);
		}
		// 2. determine which command comes next (inside conditional, inside else, or after conditional)
		if (valid) {
			calculatedNext = block; // go inside the block
			isNested = true;
		} else if (includeElse) {
			calculatedNext = elseBlock; // go inside the else
			isNested = true;
		} else {
			calculatedNext = getNext(); // fuck it, next instruction
			isNested = false;
		}
		// 3. mark command for completion
		complete();
	}

	private boolean testCondition(String valueA, String valueB, ConditionalComparison comp) {
		switch (comp) {
		case OPT_0_EQU:
			return getValue(valueA) == getValue(valueB);
		case OPT_1_NEQ:
			return getValue(valueA) != getValue(valueB);
		case OPT_2_GT:
			return getValue(valueA) > getValue(valueB);
		case OPT_3_GTE:
			return getValue(valueA) >= getValue(valueB);
		case OPT_4_LT:
			return getValue(valueA) < getValue(valueB);
		case OPT_5_LTE:
			return getValue(valueA) <= getValue(valueB);
		default:
			return false;
		}
	}

	@Override
	public void update() {

	}

}
