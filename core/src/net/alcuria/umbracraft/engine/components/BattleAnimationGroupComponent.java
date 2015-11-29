package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.BattleAnimationGroupDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

/** A component to handle simple battle animations. Contrary to the
 * {@link AnimationGroupComponent}, no logic is done to determine which pose to
 * display. Instead, by default it is set to an idle pose and then it is up to
 * the programmer to call
 * {@link BattleAnimationGroupComponent#setPose(BattlePose)} when a pose change
 * is desired.
 * @author Andrew Keturi */
public class BattleAnimationGroupComponent implements Component {

	public static enum BattlePose {
		APPROACH, ATTACK, DEAD, HURT, IDLE, RETURN
	}

	private ObjectMap<BattlePose, AnimationComponent> animations;
	private AnimationComponent currentComponent;
	private final BattleAnimationGroupDefinition definition;
	private BattlePose delayPose;
	private float delaySeconds = 0;
	private boolean isMirrored;
	private Vector2 origin = new Vector2();

	/** @param definition the {@link BattleAnimationGroupDefinition} from the
	 *        database. */
	public BattleAnimationGroupComponent(BattleAnimationGroupDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create(Entity entity) {
		if (definition != null) {
			//FIXME: ugly
			animations = new ObjectMap<BattlePose, AnimationComponent>();
			animations.put(BattlePose.APPROACH, new AnimationComponent(Game.db().anim(definition.towards)));
			animations.put(BattlePose.ATTACK, new AnimationComponent(Game.db().anim(definition.attack)));
			animations.put(BattlePose.DEAD, new AnimationComponent(Game.db().anim(definition.dead)));
			animations.put(BattlePose.HURT, new AnimationComponent(Game.db().anim(definition.hurt)));
			animations.put(BattlePose.IDLE, new AnimationComponent(Game.db().anim(definition.idle)));
			animations.put(BattlePose.RETURN, new AnimationComponent(Game.db().anim(definition.away)));
			for (AnimationComponent anim : animations.values()) {
				anim.create(entity);
				anim.setMirrorAll(isMirrored);
				anim.setOrigin(origin);
			}
			currentComponent = animations.get(BattlePose.IDLE);
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {
		if (currentComponent != null) {
			currentComponent.render(entity);
		}
	}

	/** Delays for some time before changing the pose. Calls are not queued;
	 * calling this multiple times before the pose has changed will cancel any
	 * prior calls scheduled.
	 * @param delaySeconds the time in seconds to wait before changing the pose
	 * @param pose the pose to change to after some time has passed */
	public void setDelayedPose(float delaySeconds, BattlePose delayPose) {
		this.delaySeconds = delaySeconds;
		this.delayPose = delayPose;
	}

	/** Sets whether or not to mirror all animations
	 * @param isMirrored */
	public void setMirrorAll(boolean isMirrored) {
		this.isMirrored = isMirrored;
		if (animations != null) {
			for (AnimationComponent anim : animations.values()) {
				anim.setMirrorAll(isMirrored);
			}
			currentComponent.setMirrorAll(isMirrored);
		}
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}

	/** Sets the current pose to render for this component. Be sure
	 * {@link BattleAnimationGroupComponent#create(Entity)} has been called.
	 * @param pose the {@link BattlePose}. */
	public void setPose(BattlePose pose) {
		if (currentComponent != null) {
			currentComponent = animations.get(pose);
			currentComponent.reset();
		}
	}

	@Override
	public void update(Entity entity) {
		if (currentComponent != null) {
			currentComponent.update(entity);
		}
		if (delaySeconds > 0) {
			delaySeconds -= Gdx.graphics.getDeltaTime();
			if (delaySeconds <= 0 && delayPose != null) {
				setPose(delayPose);
				delayPose = null;
			}
		}
	}
}
