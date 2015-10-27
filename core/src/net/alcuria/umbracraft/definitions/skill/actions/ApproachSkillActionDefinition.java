package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

public class ApproachSkillActionDefinition extends SkillActionDefinition {

	private final float duration = 0.5f;
	private final Vector3 start = new Vector3(), target = new Vector3();
	private float timer;

	@Override
	public void start(Entity entity, Vector3 start, Vector3 target) {
		this.start.set(entity.position);
		this.target.set(target);
		timer = 0;
	}

	@Override
	public void update(Entity entity, Listener stepCompleteListener) {
		if (timer < duration) {
			entity.position.x = start.x + ((target.x - start.x) * Interpolation.pow2Out.apply(timer / duration));
			entity.position.y = start.y + ((target.y - start.y) * Interpolation.pow2Out.apply(timer / duration));
			timer += Gdx.graphics.getDeltaTime();
			if (timer >= duration) {
				entity.position.x = target.x;
				entity.position.y = target.y;
				stepCompleteListener.invoke();
			}
		}
	}

}
