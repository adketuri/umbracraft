package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class ProjectileSkillActionDefinition extends SkillActionDefinition {

	@Tooltip("The projectile animation")
	public String animation;
	@Tooltip("The time it takes for the projectile")
	public float time;
	@Tooltip("The Y offset of the projectile")
	public float yOffset;

	@Override
	public SkillActionDefinition cpy() {
		ProjectileSkillActionDefinition def = new ProjectileSkillActionDefinition();
		def.time = time;
		def.yOffset = yOffset;
		def.animation = animation;
		return def;
	}

}
