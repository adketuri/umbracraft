package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class WaitSkillActionDefinition extends SkillActionDefinition {
	@Tooltip("The amount of time to wait, in seconds.")
	public float wait;

	/** For serialization */
	public WaitSkillActionDefinition() {
	}

	public WaitSkillActionDefinition(float wait) {
		this.wait = wait;
	}
}
