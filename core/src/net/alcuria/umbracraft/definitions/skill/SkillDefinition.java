package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

public class SkillDefinition extends Definition {

	@Tooltip("The multiplier of the base damage")
	public float damageMultiplier;
	@Tooltip("The cost to use the skill, in EP")
	public int epCost;
	public float maxPercentageCost;
	@Tooltip("The name of the skill")
	public String name;
	public float turnCost;

	@Override
	public String getName() {
		return name != null ? name : "Skill";
	}

}
