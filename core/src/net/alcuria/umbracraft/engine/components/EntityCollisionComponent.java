package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/** A component for handling when an entity overlaps with another entity. Once
 * all entities are created, {@link EntityCollisionComponent#setEntities(Array)}
 * needs to be called so this component knows about all other entities.
 * @author Andrew Keturi */
public class EntityCollisionComponent implements Component {

	private final Rectangle r1 = new Rectangle(), r2 = new Rectangle();
	private final Vector2 v1 = new Vector2(), v2 = new Vector2();
	private final Vector2 v3 = new Vector2();

	@Override
	public void create(Entity entity) {

	}

	@Override
	public void dispose(Entity entity) {

	}

	public Rectangle getBounds() {
		return r1;
	}

	@Override
	public void render(Entity entity) {
	}

	@Override
	public void update(Entity entity) {
		for (Entity otherEntity : Game.entities().get()) {
			// ensure it's another entity and it's on the same z axis
			if (otherEntity != entity && MathUtils.isEqual(entity.position.z, otherEntity.position.z, 2f)) {
				MapCollisionComponent component = entity.getComponent(MapCollisionComponent.class);
				MapCollisionComponent otherComponent = otherEntity.getComponent(MapCollisionComponent.class);
				if (component != null && otherComponent != null) {
					r1.set(entity.position.x, entity.position.y, component.getWidth(), component.getHeight());
					r2.set(otherEntity.position.x, otherEntity.position.y, otherComponent.getWidth(), otherComponent.getHeight());
					if (r1.overlaps(r2)) {
						// determine the magnitude of overlap for both x and y
						float overlapX = Math.min(Math.abs(r2.x + r2.width - r1.x), Math.abs(r1.x + r1.width - r2.x));
						float overlapY = Math.min(Math.abs(r2.y + r2.height - r1.y), Math.abs(r1.y + r1.height - r2.y));
						// if there's more x overlap we want to nudge in the y direction, and vice versa
						if (overlapX < overlapY) {
							// determine nudge sign by the x positions
							if (r1.x > r2.x) {
								entity.position.x += overlapX;
							} else {
								entity.position.x -= overlapX;
							}
						} else {
							// determine nudge sign by the y positions
							if (r1.y > r2.y) {
								entity.position.y += overlapY;
							} else {
								entity.position.y -= overlapY;
							}
						}
					}
				}
			}
		}
	}

}
