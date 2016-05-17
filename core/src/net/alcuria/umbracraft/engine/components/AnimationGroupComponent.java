package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent.Pose;
import net.alcuria.umbracraft.engine.entities.Entity;

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
	private boolean cardinalOnly = false;
	private AnimationComponent currentComponent;
	private Direction currentDirection;
	private final AnimationGroupDefinition definition;
	private final String template;
	private final Pose templatePose;
	private final int templateX, templateY;

	public AnimationGroupComponent(AnimationGroupDefinition definition) {
		this(definition, null, null, 0, 0);
	}

	public AnimationGroupComponent(AnimationGroupDefinition definition, String template, Pose templatePose, int templateX, int templateY) {
		this.definition = definition;
		this.template = template;
		this.templateX = templateX;
		this.templateY = templateY;
		this.templatePose = templatePose;
		cardinalOnly = definition != null ? definition.cardinalOnly : false;
	}

	@Override
	public void create(Entity entity) {
		animations = new ObjectMap<Direction, AnimationComponent>();
		animations.put(Direction.DOWN, new AnimationComponent(template, templateX, templateY, templatePose, Direction.DOWN, definition != null ? Game.db().anim(definition.down) : null));
		animations.put(Direction.LEFT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.LEFT, definition != null ? Game.db().anim(definition.left) : null));
		animations.put(Direction.RIGHT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.RIGHT, definition != null ? Game.db().anim(definition.right) : null));
		animations.put(Direction.UP, new AnimationComponent(template, templateX, templateY, templatePose, Direction.UP, definition != null ? Game.db().anim(definition.up) : null));
		animations.put(Direction.DOWNLEFT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.DOWNLEFT, definition != null ? Game.db().anim(definition.cardinalOnly ? definition.down : definition.downLeft) : null));
		animations.put(Direction.DOWNRIGHT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.DOWNRIGHT, definition != null ? Game.db().anim(definition.cardinalOnly ? definition.down : definition.downRight) : null));
		animations.put(Direction.UPLEFT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.UPLEFT, definition != null ? Game.db().anim(definition.cardinalOnly ? definition.down : definition.upLeft) : null));
		animations.put(Direction.UPRIGHT, new AnimationComponent(template, templateX, templateY, templatePose, Direction.UPRIGHT, definition != null ? Game.db().anim(definition.cardinalOnly ? definition.down : definition.upRight) : null));
		for (AnimationComponent anim : animations.values()) {
			anim.create(entity);
		}
		currentComponent = animations.get(Direction.DOWN);
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

	/** @param cardinalOnly if <code>true</code> this component will only move in
	 *        4-directions. */
	public void setCardinalOnly(boolean cardinalOnly) {
		this.cardinalOnly = cardinalOnly;
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
		if (tmpDirection != null && (!tmpDirection.isCardinal() && !cardinalOnly || tmpDirection.isCardinal() && cardinalOnly) && tmpDirection != currentDirection) {
			currentDirection = tmpDirection;
			currentComponent = animations.get(tmpDirection);
		}
		// update actual current animation
		if (currentComponent != null) {
			currentComponent.update(entity);
		}
	}

}
