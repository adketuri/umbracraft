package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class WaitSkillActionDefinition extends SkillActionDefinition {

	private float cur;
	private final float time;

	public WaitSkillActionDefinition(float time) {
		this.time = time;
	}

	@Override
	public void start(Entity entity, Vector3 start, Vector3 target) {
		cur = 0;
	}

	@Override
	public void update(Entity entity, Listener stepCompleteListener) {
		cur += Gdx.graphics.getDeltaTime();
		if (cur >= time) {
			stepCompleteListener.invoke();
		}
	}

}
