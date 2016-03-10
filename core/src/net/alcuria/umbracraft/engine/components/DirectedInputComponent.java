package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
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
	private final Pathfinder pathfinder;
	private final Vector2 target = new Vector2();
	private int targetX, targetY, currentX, currentY;

	public DirectedInputComponent() {
		pathfinder = new Pathfinder(this);
	}

	@Override
	public void create(Entity entity) {
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
		pathfinder.setTarget(new PathNode(currentX, currentY), new PathNode(x, y));
	}

	@Override
	public void update(Entity entity) {
		// stop any movement from the past frame
		currentX = (int) (entity.position.x / Config.tileWidth);
		currentY = (int) (entity.position.y / Config.tileWidth);

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
		if (targetX == currentX && targetY == currentY) {
			choseNextNode = false;
			pathfinder.getSolution().removeIndex(pathfinder.getSolution().size - 1);
			return;
		}

		target.set(targetX * Config.tileWidth + Config.tileWidth / 2, targetY * Config.tileWidth + Config.tileWidth / 2);
		target.sub(entity.position.x, entity.position.y);
		target.setLength(Math.min(target.len(), 2));
		entity.velocity.set(target.x, target.y, entity.velocity.z);
	}
}