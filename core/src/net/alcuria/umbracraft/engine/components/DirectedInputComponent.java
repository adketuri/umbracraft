package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.Pathfinder;
import net.alcuria.umbracraft.engine.Pathfinder.PathNode;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/** A component for handling input directed by some other component (for
 * instance, a {@link ScriptComponent}.
 * @author Andrew Keturi */
public class DirectedInputComponent implements Component {
	private static final float TOLERANCE = 0f; // nonzero screws shit up
	private boolean choseNextNode;
	private Direction direction;
	private Entity entity;
	private final Vector3 lastPosition = new Vector3();
	private final Pathfinder pathfinder;
	private final Vector2 target = new Vector2();
	private int targetX, targetY, currentX, currentY;

	public DirectedInputComponent() {
		pathfinder = new Pathfinder(this);
	}

	@Override
	public void create(Entity entity) {
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

	public void resetTarget(int i, int j) {
		pathfinder.stop();
		targetX = i;
		targetY = j;
	}

	/** Sets the target to walk to
	 * @param x
	 * @param y */
	public void setTarget(int x, int y) {
		pathfinder.setTarget(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode(x, y));
	}

	@Override
	public void update(Entity entity) {
		currentX = (int) (entity.position.x / Config.tileWidth);
		currentY = (int) (entity.position.y / Config.tileWidth);

		// see if we're stuck, if so try rejiggering the pathfinder
		if (((entity.position.epsilonEquals(lastPosition, 0.01f) && (targetX != currentX || targetY != currentY)))) {
			//			setTarget(targetX, targetY); //FIXME: too many crazy npe's
			Game.log("STUCK " + targetX + " " + targetY);
			lastPosition.set(entity.position);
		}

		pathfinder.update(entity);

		// see if we really need to move
		if (pathfinder.getSolution().size <= 0) {
			entity.velocity.x *= 0.7;
			entity.velocity.y *= 0.7;
			if (entity.velocity.epsilonEquals(Vector3.Zero, 0.1f)) {
				entity.velocity.x = 0;
				entity.velocity.y = 0;
			}
			return;
		}

		if (!choseNextNode) {
			final PathNode lastNode = pathfinder.getSolution().get(pathfinder.getSolution().size - 1);
			targetX = lastNode.x;
			targetY = lastNode.y;
			choseNextNode = true;
		}
		if (targetX == currentX && targetY == currentY && pathfinder.getSolution().size > 0) {
			choseNextNode = false;
			pathfinder.getSolution().removeIndex(pathfinder.getSolution().size - 1);
			return;
		}

		// pick a new direction
		if (currentX > targetX + TOLERANCE) {
			if (currentY > targetY + TOLERANCE) {
				direction = Direction.DOWNLEFT;
			} else if (currentY < targetY + TOLERANCE) {
				direction = Direction.UPLEFT;
			} else {
				direction = Direction.LEFT;
			}
		} else if (currentX < targetX + TOLERANCE) {
			if (currentY > targetY + TOLERANCE) {
				direction = Direction.DOWNRIGHT;
			} else if (currentY < targetY + TOLERANCE) {
				direction = Direction.UPRIGHT;
			} else {
				direction = Direction.RIGHT;
			}
		} else {
			if (currentY > targetY + TOLERANCE) {
				direction = Direction.DOWN;
			} else if (currentY < targetY + TOLERANCE) {
				direction = Direction.UP;
			}
		}
		lastPosition.set(entity.position);
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