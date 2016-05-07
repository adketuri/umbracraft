package net.alcuria.umbracraft.engine.components;

import net.alcuria.gen.R;
import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.Pathfinder;
import net.alcuria.umbracraft.engine.Pathfinder.PathNode;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/** A component for handling input directed by some other component (for
 * instance, a {@link ScriptComponent}.
 * @author Andrew Keturi */
public class DirectedInputComponent implements Component {
	private static final float TOLERANCE = 0.4f;
	private boolean choseNextNode;
	private final Texture debug;
	private Direction direction;
	private Entity entity;
	private final Vector3 lastPosition = new Vector3();
	private final Pathfinder pathfinder;
	private float targetX, targetY, currentX, currentY;

	public DirectedInputComponent() {
		pathfinder = new Pathfinder(this);
		debug = Game.assets().get(R.debug, Texture.class);
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
		if (Game.isDebug()) {
			Game.batch().setColor(Color.RED);
			Game.batch().draw(debug, targetX * 16 - 1, targetY * 16 - 1, 3, 3);
			Game.batch().setColor(Color.BLUE);
			Game.batch().draw(debug, currentX * 16 - 1, currentY * 16 - 1, 3, 3);
			Game.batch().setColor(Color.WHITE);
		}
	}

	public void renderPaths() {
		pathfinder.renderPaths();
	}

	public void resetTarget(int i, int j) {
		pathfinder.stop();
		targetX = i + 0.5f;
		targetY = j + 0.5f;
	}

	/** Sets the target to walk to
	 * @param x
	 * @param y */
	public void setTarget(int x, int y) {
		pathfinder.setTarget(new PathNode((int) (entity.position.x / Config.tileWidth), (int) (entity.position.y / Config.tileWidth)), new PathNode(x, y));
	}

	@Override
	public void update(Entity entity) {
		currentX = (entity.position.x / Config.tileWidth);
		currentY = (entity.position.y / Config.tileWidth);

		// see if we're stuck, if so try rejiggering the pathfinder
		if (((entity.position.epsilonEquals(lastPosition, 0.01f) && (targetX != currentX || targetY != currentY)))) {
			//			setTarget(targetX, targetY); //FIXME: too many crazy npe's
			//			Game.log("STUCK " + targetX + " " + targetY);
			//			lastPosition.set(entity.position);
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
			targetX = lastNode.x + 0.5f;
			targetY = lastNode.y + 0.5f;
			choseNextNode = true;
		}
		if (MathUtils.isEqual(targetX, currentX, 1) && MathUtils.isEqual(targetY, currentY, 1) && pathfinder.getSolution().size > 0) {
			choseNextNode = false;
			pathfinder.getSolution().removeIndex(pathfinder.getSolution().size - 1);
			return;
		}

		// pick a new direction
		float dX = Math.abs(targetX - currentX);
		float dY = Math.abs(targetY - currentY);
		direction = null;
		boolean horizontal = dX > TOLERANCE;
		boolean vertical = dY > TOLERANCE;
		if (horizontal && !vertical) {
			// left / right
			if (targetX > currentX) {
				direction = Direction.RIGHT;
			} else {
				direction = Direction.LEFT;
			}
		} else if (!horizontal && vertical) {
			if (targetY > currentY) {
				direction = Direction.UP;
			} else {
				direction = Direction.DOWN;
			}
		} else if (horizontal && vertical) {
			if (targetY > currentY) {
				if (targetX > currentX) {
					direction = Direction.UPRIGHT;
				} else {
					direction = Direction.UPLEFT;
				}
			} else {
				if (targetX > currentX) {
					direction = Direction.DOWNRIGHT;
				} else {
					direction = Direction.DOWNLEFT;
				}
			}

		}
		if (direction == null) {
			return;
		}

		lastPosition.set(entity.position);
		// update velocity
		switch (direction) {
		case DOWN:
			entity.velocity.y = -2;
			entity.velocity.x = 0;
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
			entity.velocity.y = 0;
			break;
		case RIGHT:
			entity.velocity.x = 2;
			entity.velocity.y = 0;
			break;
		case UP:
			entity.velocity.y = 2;
			entity.velocity.x = 0;
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