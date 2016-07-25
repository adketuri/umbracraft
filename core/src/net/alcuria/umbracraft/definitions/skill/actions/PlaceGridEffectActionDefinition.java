package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class PlaceGridEffectActionDefinition extends SkillActionDefinition {

	@Tooltip("An animation displayed on-tile")
	public String animation;
	@Tooltip("A unique id. Not sure what this will do yet.")
	public String id;
	@Tooltip("ID of the skill to invoke for damage, etc, on tick")
	public String skillId;
	@Tooltip("The duration of the effect")
	public int turns;

	@Override
	public SkillActionDefinition cpy() {
		PlaceGridEffectActionDefinition def = new PlaceGridEffectActionDefinition();
		def.animation = animation;
		def.id = id;
		def.turns = turns;
		def.skillId = skillId;
		return def;
	}
}
