package net.alcuria.umbracraft.definitions.skill.actions;

public class PlaceGridEffectActionDefinition extends SkillActionDefinition {

	public String animation;
	public String id;
	public int turns;

	@Override
	public SkillActionDefinition cpy() {
		PlaceGridEffectActionDefinition def = new PlaceGridEffectActionDefinition();
		def.animation = animation;
		def.id = id;
		def.turns = turns;
		return def;
	}
}
