package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.ObjectMap;

/** A component used for displaying components in particular directions.
 * @author Andrew Keturi */
public class AnimationGroupComponent implements BaseComponent {

	public static enum Direction {
		DOWN, DOWNLEFT, DOWNRIGHT, LEFT, RIGHT, UP, UPLEFT, UPRIGHT
	}

	private ObjectMap<Direction, AnimationComponent> animations;
	private AnimationComponent currentComponent;
	private final AnimationGroupDefinition definition;
	private Direction lastDirection;

	public AnimationGroupComponent(AnimationGroupDefinition definition) {
		this.definition = definition;
	}

	@Override
	public void create() {
		if (definition != null) {
			animations = new ObjectMap<Direction, AnimationComponent>();
			//FIXME: ugly
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

	@Override
	public void render(Entity object) {
		if (currentComponent != null) {
			currentComponent.render(object);
		}
	}

	@Override
	public void update(Entity object) {
		Direction currentDirection = lastDirection;
		int mask = 0b0000; // up, down, left, right
		if (object.velocity.x < 0) {
			mask = mask ^ 0b0010;
		} else if (object.velocity.x > 0) {
			mask = mask ^ 0b0001;
		}
		if (object.velocity.y < 0) {
			mask = mask ^ 0b0100;
		} else if (object.velocity.y > 0) {
			mask = mask ^ 0b1000;
		}
		switch (mask) {
		case 0b0001:
			currentDirection = Direction.RIGHT;
			break;
		case 0b0010:
			currentDirection = Direction.LEFT;
			break;
		case 0b0100:
			currentDirection = Direction.DOWN;
			break;
		case 0b1000:
			currentDirection = Direction.UP;
			break;
		case 0b1010:
			currentDirection = Direction.UPLEFT;
			break;
		case 0b1001:
			currentDirection = Direction.UPRIGHT;
			break;
		case 0b0101:
			currentDirection = Direction.DOWNRIGHT;
			break;
		case 0b0110:
			currentDirection = Direction.DOWNLEFT;
			break;
		}
		if (currentDirection != lastDirection) {
			lastDirection = currentDirection;
			currentComponent = animations.get(currentDirection);
		}
		currentComponent.update(object);
	}

}
