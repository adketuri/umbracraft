package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.definitions.Definition;

public class SkillDefinition extends Definition {

	public int cooldown;
	public int cost;
	public float damageMultiplier;
	public float maxPercentageCost;
	public String name;
	public float turnCost;

	@Override
	public String getName() {
		return name != null ? name : "Skill";
	}

}
