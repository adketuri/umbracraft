package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.ObjectMap;

/** A component containing a collection of groups. Handles logic for updating the
 * reference to the current group based on the entity's velocity, position, and
 * so on.
 * @author Andrew Keturi */
public class AnimationCollectionComponent implements BaseComponent {

	public static enum Pose {
		FALLING, IDLE, JUMPING, RUNNING, WALKING
	}

	private Direction currentDirection;
	private AnimationGroupComponent currentGroup;
	private Pose currentPose;
	private final AnimationCollectionDefinition definition;

	private ObjectMap<Pose, AnimationGroupComponent> groups;

	public AnimationCollectionComponent(AnimationCollectionDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create(Entity entity) {
		if (definition != null) {
			//FIXME: ugly
			groups = new ObjectMap<Pose, AnimationGroupComponent>();
			groups.put(Pose.IDLE, new AnimationGroupComponent(Game.db().animGroup(definition.idle)));
			groups.put(Pose.WALKING, new AnimationGroupComponent(Game.db().animGroup(definition.walking)));
			groups.put(Pose.FALLING, new AnimationGroupComponent(Game.db().animGroup(definition.falling)));
			groups.put(Pose.JUMPING, new AnimationGroupComponent(Game.db().animGroup(definition.jumping)));
			groups.put(Pose.RUNNING, new AnimationGroupComponent(Game.db().animGroup(definition.running)));
			for (AnimationGroupComponent anim : groups.values()) {
				anim.create(entity);
			}
			currentGroup = groups.get(Pose.IDLE);
		}
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {
		if (currentGroup != null) {
			currentGroup.render(entity);
		}
	}

	@Override
	public void update(Entity entity) {
		//save off last pose and get current pose/direction
		Pose lastPose = currentPose;
		if (entity.velocity.x == 0 && entity.velocity.y == 0) {
			currentPose = Pose.IDLE;
		} else {
			currentPose = Pose.WALKING;
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
