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
			collision = entity.getComponent(MapCollisionComponent.class);
			//FIXME: ugly
			groups = new ObjectMap<Pose, AnimationGroupComponent>();
			groups.put(Pose.IDLE, new AnimationGroupComponent(Game.db().animGroup(definition.idle)));
			groups.put(Pose.WALKING, new AnimationGroupComponent(Game.db().animGroup(definition.walking)));
			groups.put(Pose.FALLING, new AnimationGroupComponent(Game.db().animGroup(definition.falling)));
			groups.put(Pose.JUMPING, new AnimationGroupComponent(Game.db().animGroup(definition.jumping)));
			groups.put(Pose.RUNNING, new AnimationGroupComponent(Game.db().animGroup(definition.running)));
			groups.put(Pose.INSPECT, new AnimationGroupComponent(Game.db().animGroup(definition.inspect)));
			for (AnimationGroupComponent anim : groups.values()) {
				anim.create(entity);
			}
			currentGroup = groups.get(Pose.IDLE);
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	public AnimationGroupComponent getGroup() {
		return currentGroup;
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
			} else {
				currentPose = Pose.WALKING;
				currentDirection = currentGroup.getDirection();
			}
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
