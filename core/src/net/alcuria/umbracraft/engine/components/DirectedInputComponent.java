package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.engine.Pathfinder;
import net.alcuria.umbracraft.engine.Pathfinder.PathNode;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

/** A component for handling input directed by some other component (for
 * instance, a {@link ScriptComponent}.
 * @author Andrew Keturi */
public class DirectedInputComponent implements Component {

	private Direction direction;
	private Entity entity;
	private boolean haltMovement;
	private final Pathfinder pathfinder;

	public DirectedInputComponent() {
		pathfinder = new Pathfinder(this);
	}

	@Override
	public void create(Entity entity) {
		haltMovement = true;
		this.entity = entity;
	}

	@Override
	public void dispose(Entity entity) {
	}

	@Override
	public void render(Entity entity) {

	}

	public void renderPaths() {
		pathfinder.renderPaths();
	}

	/** Sets the target to walk to
	 * @param x
	 * @param y */
	public void setTarget(int x, int y) {
		haltMovement = false;
		pathfinder.setDestination(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode(x, y));
	}

	@Override
	public void update(Entity entity) {
		// stop any movement from the past frame
		entity.velocity.x = 0;
		entity.velocity.y = 0;

		pathfinder.update(entity);

		// see if we really need to move
		if (haltMovement || pathfinder.isRunning()) {
			return;
		}

		//		// pick a new direction
		//		if (entity.position.x > target.x) {
		//			direction = Direction.LEFT;
		//		} else if (entity.position.x < target.x) {
		//			direction = Direction.RIGHT;
		//		}
		//		if (entity.position.y > target.y) {
		//			direction = Direction.DOWN;
		//		} else if (entity.position.y < target.y) {
		//			direction = Direction.UP;
		//		}
		//
		//		// update velocity
		//		switch (direction) {
		//		case DOWN:
		//			entity.velocity.y = -2;
		//			break;
		//		case DOWNLEFT:
		//			entity.velocity.x = -2 * 0.707f;
		//			entity.velocity.y = -2 * 0.707f;
		//			break;
		//		case DOWNRIGHT:
		//			entity.velocity.x = 2 * 0.707f;
		//			entity.velocity.y = -2 * 0.707f;
		//			break;
		//		case LEFT:
		//			entity.velocity.x = -2;
		//			break;
		//		case RIGHT:
		//			entity.velocity.x = 2;
		//			break;
		//		case UP:
		//			entity.velocity.y = 2;
		//			break;
		//		case UPLEFT:
		//			entity.velocity.x = -2 * 0.707f;
		//			entity.velocity.y = 2 * 0.707f;
		//			break;
		//		case UPRIGHT:
		//			entity.velocity.x = 2 * 0.707f;
		//			entity.velocity.y = 2 * 0.707f;
		//			break;
		//		default:
		//			break;
		//		}
	}
}