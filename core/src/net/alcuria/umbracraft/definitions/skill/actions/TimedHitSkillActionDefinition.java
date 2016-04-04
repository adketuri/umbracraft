package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

public class TimedHitSkillActionDefinition extends SkillActionDefinition {
	@Tooltip("The length of time the player has (in secs) to get a timed hit")
	public float window;

}
