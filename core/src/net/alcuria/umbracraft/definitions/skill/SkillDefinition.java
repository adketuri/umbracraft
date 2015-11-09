package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

public class SkillDefinition extends Definition {

	@Tooltip("The multiplier of the base damage")
	public float damageMultiplier;
	@Tooltip("The cost to use the skill, in EP")
	public int epCost;
	@Tooltip("The icon to use for the skill")
	public String iconId;
	public float maxPercentageCost;
	@Tooltip("The name of the skill")
	public String name;
	@Tooltip("The targets of the skill, relative to the drop point")
	public Array<SkillPositionDefinition> target;
	@Tooltip("The ATB cost of the skill, in turns")
	public float turnCost;

	@Override
	public String getName() {
		return name != null ? name : "Skill";
	}

}
