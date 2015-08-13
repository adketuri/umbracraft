package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;

public class DirectedInputComponent implements Component {

	private Direction direction;
	private boolean haltMovement;

	@Override
	public void create(Entity entity) {

	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {

	}

	@Override
	public void update(Entity entity) {
		// update velocity
		entity.velocity.x = 0;
		entity.velocity.y = 0;
		if (haltMovement) {
			return;
		}
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
