package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.components.BattleAnimationGroupComponent;
import net.alcuria.umbracraft.engine.components.BattleAnimationGroupComponent.BattlePose;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.Vector3;

public class PoseChangeDefinition extends SkillActionDefinition {
	private final BattlePose pose;

	public PoseChangeDefinition(BattlePose pose) {
		this.pose = pose;
	}

	@Override
	public void start(Entity entity, Vector3 start, Vector3 target) {
		entity.getComponent(BattleAnimationGroupComponent.class).setPose(pose);
	}

	@Override
	public void update(Entity entity, Listener stepCompleteListener) {
		stepCompleteListener.invoke();
	}

}
