package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.engine.components.BattleAnimationGroupComponent.BattlePose;

public class PoseChangeActionDefinition extends SkillActionDefinition {
	@Tooltip("The new pose to change to.")
	public BattlePose pose;

	/** For serialization */
	public PoseChangeActionDefinition() {
	}

	public PoseChangeActionDefinition(BattlePose pose) {
		this.pose = pose;
	}
}
