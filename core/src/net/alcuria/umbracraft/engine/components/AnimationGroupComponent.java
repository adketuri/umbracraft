package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.ObjectMap;

/** A component used for displaying components in particular directions.
 * @author Andrew Keturi */
public class AnimationGroupComponent implements BaseComponent {

	/** Various animation facing directions
	 * @author Andrew Keturi */
	public static enum Direction {
		DOWN, DOWNLEFT, DOWNRIGHT, LEFT, RIGHT, UP, UPLEFT, UPRIGHT
	}

	private ObjectMap<Direction, AnimationComponent> animations;
	private AnimationComponent currentComponent;
	private final AnimationGroupDefinition definition;
	private Direction currentDirection;

	public AnimationGroupComponent(AnimationGroupDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create() {
		if (definition != null) {
			//FIXME: ugly
			animations = new ObjectMap<Direction, AnimationComponent>();
			animations.put(Direction.DOWN, new AnimationComponent(Game.db().anim(definition.down)));
			animations.put(Direction.LEFT, new AnimationComponent(Game.db().anim(definition.left)));
			animations.put(Direction.RIGHT, new AnimationComponent(Game.db().anim(definition.right)));
			animations.put(Direction.UP, new AnimationComponent(Game.db().anim(definition.up)));
			animations.put(Direction.DOWNLEFT, new AnimationComponent(Game.db().anim(definition.downLeft)));
			animations.put(Direction.DOWNRIGHT, new AnimationComponent(Game.db().anim(definition.downRight)));
			animations.put(Direction.UPLEFT, new AnimationComponent(Game.db().anim(definition.upLeft)));
			animations.put(Direction.UPRIGHT, new AnimationComponent(Game.db().anim(definition.upRight)));
			for (AnimationComponent anim : animations.values()) {
				anim.create();
			}
			currentComponent = animations.get(Direction.DOWN);
		}
	}

	@Override
	public void dispose() {

	}

	public Direction getDirection() {
		return currentDirection;
	}

	@Override
	public void render(Entity entity) {
		if (currentComponent != null) {
			currentComponent.render(entity);
		}
	}

	public void setDirection(Direction direction) {
		if (direction == null) {
			return;
		}
		currentDirection = direction;
		if (animations != null) {
			currentComponent = animations.get(currentDirection);
		}
	}

	@Override
	public void update(Entity entity) {
		Direction tmpDirection = currentDirection;
		// set mask based on velocity of player
		int mask = 0b0000; // up, down, left, right
		if (entity.velocity.x < 0) {
			mask = mask ^ 0b0010;
		} else if (entity.velocity.x > 0) {
			mask = mask ^ 0b0001;
		}
		if (entity.velocity.y < 0) {
			mask = mask ^ 0b0100;
		} else if (entity.velocity.y > 0) {
			mask = mask ^ 0b1000;
		}
		// update current facing direction
		switch (mask) {
		case 0b0001:
			tmpDirection = Direction.RIGHT;
			break;
		case 0b0010:
			tmpDirection = Direction.LEFT;
			break;
		case 0b0100:
			tmpDirection = Direction.DOWN;
			break;
		case 0b1000:
			tmpDirection = Direction.UP;
			break;
		case 0b1010:
			tmpDirection = Direction.UPLEFT;
			break;
		case 0b1001:
			tmpDirection = Direction.UPRIGHT;
			break;
		case 0b0101:
			tmpDirection = Direction.DOWNRIGHT;
			break;
		case 0b0110:
			tmpDirection = Direction.DOWNLEFT;
			break;
		}
		// if it's different, update reference to the current component
		if (tmpDirection != currentDirection) {
			currentDirection = tmpDirection;
			currentComponent = animations.get(tmpDirection);
		}
		// update actual current animation
		currentComponent.update(entity);
	}

}
