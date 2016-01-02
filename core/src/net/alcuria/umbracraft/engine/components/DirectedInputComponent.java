package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.Vector2;

/** A component for handling input directed by some other component (for
 * instance, a {@link ScriptComponent}. TODO: pathfinding!
 * @author Andrew Keturi */
public class DirectedInputComponent implements Component {

	private Direction direction;
	private boolean haltMovement;
	private final Vector2 target = new Vector2();

	@Override
	public void create(Entity entity) {
		target.x = entity.position.x;
		target.y = entity.position.y;
		haltMovement = true;
	}

	@Override
	public void dispose(Entity entity) {
	}

	@Override
	public void render(Entity entity) {

	}

	/** Sets the target to walk to
	 * @param x
	 * @param y */
	public void setTarget(int x, int y) {
		target.x = x;
		target.y = y;
		haltMovement = false;
	}

	@Override
	public void update(Entity entity) {
		// stop any movement from the past frame
		entity.velocity.x = 0;
		entity.velocity.y = 0;

		// see if we really need to move
		if (haltMovement || entity.position.epsilonEquals(target.x, target.y, entity.position.z, 1f)) {
			return;
		}

		// pick a new direction
		if (entity.position.x > target.x) {
			direction = Direction.LEFT;
		} else if (entity.position.x < target.x) {
			direction = Direction.RIGHT;
		}
		if (entity.position.y > target.y) {
			direction = Direction.DOWN;
		} else if (entity.position.y < target.y) {
			direction = Direction.UP;
		}

		// update velocity
		switch (direction) {
		case DOWN:
			entity.velocity.y = -2;
			break;
		case DOWNLEFT:
			entity.velocity.x = -2 * 0.707f;
			entity.velocity.y = -2 * 0.707f;
			break;
		case DOWNRIGHT:
			entity.velocity.x = 2 * 0.707f;
			entity.velocity.y = -2 * 0.707f;
			break;
		case LEFT:
			entity.velocity.x = -2;
			break;
		case RIGHT:
			entity.velocity.x = 2;
			break;
		case UP:
			entity.velocity.y = 2;
			break;
		case UPLEFT:
			entity.velocity.x = -2 * 0.707f;
			entity.velocity.y = 2 * 0.707f;
			break;
		case UPRIGHT:
			entity.velocity.x = 2 * 0.707f;
			entity.velocity.y = 2 * 0.707f;
			break;
		default:
			break;
		}
	}
}