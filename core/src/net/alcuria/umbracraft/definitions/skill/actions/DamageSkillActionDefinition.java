package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;

/** Handles definitions for a skill's damage component. Actual damage amounts
 * come from the {@link SkillDefinition} but percentages of that damage may be
 * dealt as actions.
 * @author Andrew Keturi */
public class DamageSkillActionDefinition extends SkillActionDefinition {

	@Tooltip("The amount of damage to deal as a percent of the skill's total damage")
	public float percentDamage;

	@Override
	public SkillActionDefinition cpy() {
		DamageSkillActionDefinition def = new DamageSkillActionDefinition();
		def.percentDamage = percentDamage;
		return def;
	}

}
