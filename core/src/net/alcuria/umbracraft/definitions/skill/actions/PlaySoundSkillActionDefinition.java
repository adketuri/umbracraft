package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class PlaySoundSkillActionDefinition extends SkillActionDefinition {

	@Tooltip("The sound to play")
	public String sound;

	@Override
	public SkillActionDefinition cpy() {
		PlaySoundSkillActionDefinition def = new PlaySoundSkillActionDefinition();
		def.sound = sound;
		return def;
	}

}
