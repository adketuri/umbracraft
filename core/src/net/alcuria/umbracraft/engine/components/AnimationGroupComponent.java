package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.ObjectMap;

/** A component used for displaying components in particular directions.
 * @author Andrew Keturi */
public class AnimationGroupComponent implements Component {

	/** Various animation facing directions
	 * @author Andrew Keturi */
	public static enum Direction {
		DOWN, DOWNLEFT, DOWNRIGHT, LEFT, RIGHT, UP, UPLEFT, UPRIGHT;

		/** Given an angle in degrees, returns the direction at that angle. 0
		 * degrees = west, 90 degrees = south, 180 degrees = east, 270 degrees =
		 * north
		 * @param angle
		 * @return */
		public static Direction from(int angle) {
			switch (angle) {
			case 0:
			case 360:
				return Direction.LEFT;
			case 45:
				return Direction.DOWNLEFT;
			case 90:
				return Direction.DOWN;
			case 135:
				return Direction.DOWNRIGHT;
			case 180:
				return Direction.RIGHT;
			case 225:
				return Direction.UPRIGHT;
			case 270:
				return Direction.UP;
			case 315:
				return Direction.UPLEFT;
			default:
				return Direction.DOWN;
			}
		}

		/** For 2k-style spritesheet templating
		 * @return */
		public int getTemplateIndex() {
			switch (this) {
			case UP:
				return 0;
			case RIGHT:
				return 1;
			case DOWN:
				return 2;
			case LEFT:
				return 3;
			}
			return 3;
		}

		/** @return <code>true</code> if the direction is cardinal (up, down,
		 *         left, or right) */
		public boolean isCardinal() {
			return this == Direction.LEFT || this == Direction.DOWN || this == Direction.UP || this == Direction.RIGHT;
		}
	}

	private ObjectMap<Direction, AnimationComponent> animations;
	private AnimationComponent currentComponent;
	private Direction currentDirection;
	private final AnimationGroupDefinition definition;

	public AnimationGroupComponent(AnimationGroupDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create(Entity entity) {
		if (definition != null) {
			animations = new ObjectMap<Direction, AnimationComponent>();
			if (StringUtils.isNotEmpty(definition.template)) {
				for (Direction direction : Direction.values()) {
					if (definition.cardinalOnly && !direction.isCardinal()) {
						continue;
					}
					animations.put(direction, new AnimationComponent(definition.template, direction));
				}
			} else {
				animations.put(Direction.DOWN, new AnimationComponent(Game.db().anim(definition.down)));
				animations.put(Direction.LEFT, new AnimationComponent(Game.db().anim(definition.left)));
				animations.put(Direction.RIGHT, new AnimationComponent(Game.db().anim(definition.right)));
				animations.put(Direction.UP, new AnimationComponent(Game.db().anim(definition.up)));
				animations.put(Direction.DOWNLEFT, new AnimationComponent(Game.db().anim(definition.cardinalOnly ? definition.down : definition.downLeft)));
				animations.put(Direction.DOWNRIGHT, new AnimationComponent(Game.db().anim(definition.cardinalOnly ? definition.down : definition.downRight)));
				animations.put(Direction.UPLEFT, new AnimationComponent(Game.db().anim(definition.cardinalOnly ? definition.down : definition.upLeft)));
				animations.put(Direction.UPRIGHT, new AnimationComponent(Game.db().anim(definition.cardinalOnly ? definition.down : definition.upRight)));
			}
			for (AnimationComponent anim : animations.values()) {
				anim.create(entity);
			}
			currentComponent = animations.get(Direction.DOWN);
		}
	}

	@Override
	public void dispose(Entity entity) {

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
		final float tolerance = 0.5f;
		if (entity.velocity.x < -tolerance) {
			mask = mask ^ 0b0010;
		} else if (entity.velocity.x > tolerance) {
			mask = mask ^ 0b0001;
		}
		if (entity.velocity.y < -tolerance) {
			mask = mask ^ 0b0100;
		} else if (entity.velocity.y > tolerance) {
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
