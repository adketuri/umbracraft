package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.O;

import com.badlogic.gdx.utils.ObjectMap;

/** A component containing a collection of groups. Handles logic for updating the
 * reference to the current group based on the entity's velocity, position, and
 * so on.
 * @author Andrew Keturi */
public class AnimationCollectionComponent implements Component {

	public static enum Pose {
		FALLING, IDLE, INSPECT, JUMPING, RUNNING, WALKING
	}

	private MapCollisionComponent collision;
	private Direction currentDirection;
	private AnimationGroupComponent currentGroup;
	private Pose currentPose;
	private final AnimationCollectionDefinition definition;
	private ObjectMap<Pose, AnimationGroupComponent> groups;
	private boolean visible = true;

	public AnimationCollectionComponent(AnimationCollectionDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create(Entity entity) {
		if (definition != null) {
			groups = new ObjectMap<Pose, AnimationGroupComponent>();
			groups.put(Pose.IDLE, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.idle) : null, definition.template, Pose.IDLE, definition.templateX, definition.templateY));
			groups.put(Pose.WALKING, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.walking) : null, definition.template, Pose.WALKING, definition.templateX, definition.templateY));
			groups.put(Pose.FALLING, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.falling) : null, definition.template, Pose.FALLING, definition.templateX, definition.templateY));
			groups.put(Pose.JUMPING, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.jumping) : null, definition.template, Pose.JUMPING, definition.templateX, definition.templateY));
			groups.put(Pose.RUNNING, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.running) : null, definition.template, Pose.RUNNING, definition.templateX, definition.templateY));
			groups.put(Pose.INSPECT, new AnimationGroupComponent(definition.idle != null ? Game.db().animGroup(definition.inspect) : null, definition.template, Pose.INSPECT, definition.templateX, definition.templateY));
			for (AnimationGroupComponent anim : groups.values()) {
				anim.create(entity);
				anim.setCardinalOnly(definition.cardinalOnly);
			}
			currentGroup = groups.get(Pose.IDLE);
			collision = entity.getComponent(MapCollisionComponent.class);
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	public AnimationGroupComponent getGroup() {
		return currentGroup;
	}

	private boolean isJumping(Entity entity) {
		return collision != null && !collision.isOnGround();
	}

	private boolean isMoving(Entity entity) {
		if (collision != null && collision.isOnStairs() && entity.velocity.z != 0) {
			return true;
		}
		return entity.velocity.x != 0 || entity.velocity.y != 0;
	}

	@Override
	public void render(Entity entity) {
		if (currentGroup != null && visible) {
			currentGroup.render(entity);
		}
	}

	/** Sets a direction for the current active group in the collection.
	 * @param direction the {@link Direction} we want to change to */
	public void setDirection(Direction direction) {
		O.notNull(direction);
		currentGroup.setDirection(direction);
	}

	/** Sets the current {@link Pose} and marks the
	 * {@link AnimationCollectionComponent} as visible.
	 * @param pose */
	public void setPose(Pose pose) {
		setVisible(true);
		currentGroup = groups.get(pose);
		currentGroup.setDirection(currentDirection);
	}

	/** @param visible whether or not the {@link AnimationCollectionComponent}
	 *        gets rendered */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Entity entity) {
		//save off last pose and get current pose/direction
		Pose lastPose = currentPose;
		if (lastPose != Pose.INSPECT) {
			if (!isMoving(entity)) {
				currentPose = Pose.IDLE;
			} else if (isJumping(entity)) {
				currentPose = Pose.JUMPING;
			} else {
				currentPose = Pose.WALKING;
			}
			currentDirection = currentGroup.getDirection();
		}
		// if the pose has updated, update reference to currentGroup and set direction
		if (currentPose != lastPose) {
			currentGroup = groups.get(currentPose);
			currentGroup.setDirection(currentDirection);
		} else {
			currentGroup.update(entity);
		}
	}
}
