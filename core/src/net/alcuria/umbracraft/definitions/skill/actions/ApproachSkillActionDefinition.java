package net.alcuria.umbracraft.definitions.skill.actions;

public class ApproachSkillActionDefinition extends SkillActionDefinition {
	public int x, y;

	/** For serialization */
	public ApproachSkillActionDefinition() {
	}

	public ApproachSkillActionDefinition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
