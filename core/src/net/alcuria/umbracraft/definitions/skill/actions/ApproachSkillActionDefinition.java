package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class ApproachSkillActionDefinition extends SkillActionDefinition {
	@Tooltip("The x coordinate to approach, in pixels, relative to the target.")
	public int x;
	@Tooltip("The y coordinate to approach, in pixels, relative to the target.")
	public int y;

	/** For serialization */
	public ApproachSkillActionDefinition() {
	}

	public ApproachSkillActionDefinition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
