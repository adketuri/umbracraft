package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.skill.actions.SkillActionDefinition;

import com.badlogic.gdx.utils.Array;

public class SkillDefinition extends Definition {

	public static enum TargetRestriction {
		NO_RESTRICTION, REQUIRES_FREE_SPACE, REQUIRES_TARGET
	}

	@Tooltip("The action sequence when using the skill")
	public Array<SkillActionDefinition> actions;
	@Tooltip("The multiplier of the base damage")
	public float damageMultiplier;
	@Tooltip("A description of the skill to show in-battle")
	public String description;
	@Tooltip("The EP generated (positive) or EP taken (negative) to use the skill")
	public int ep;
	@Tooltip("A multiplier to the focus generated. Makes certain skills more or less likely to draw enemy attention.")
	public float focusMultiplier;
	@Tooltip("The icon to use for the skill")
	public String iconId;
	@Tooltip("The name of the skill")
	public String name;
	@Tooltip("A tag for sorting")
	public String tag;
	@Tooltip("Any restrictions on using the skill. For instance, the skill requires a target to cast or the skill must be cast into a blank space.")
	public TargetRestriction targetRestriction = TargetRestriction.NO_RESTRICTION;
	@Tooltip("The targets of the skill, relative to the drop point")
	public Array<SkillPositionDefinition> targets;
	@Tooltip("The ATB cost of the skill, in turns")
	public float turnCost;

	@Override
	public String getName() {
		return name != null ? name : "Skill";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}

}
