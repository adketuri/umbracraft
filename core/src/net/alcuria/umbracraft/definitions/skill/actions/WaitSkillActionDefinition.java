package net.alcuria.umbracraft.definitions.skill.actions;

public class WaitSkillActionDefinition extends SkillActionDefinition {
	public float wait;

	/** For serialization */
	public WaitSkillActionDefinition() {
	}

	public WaitSkillActionDefinition(float wait) {
		this.wait = wait;
	}
}
