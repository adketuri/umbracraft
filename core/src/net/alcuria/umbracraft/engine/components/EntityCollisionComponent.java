package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/** A component for handling when an entity overlaps with another entity.
 * @author Andrew Keturi */
public class EntityCollisionComponent implements Component {

	private final Rectangle r1 = new Rectangle(), r2 = new Rectangle();

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
		if (Game.isDebug()) {
			Color oldColor = Game.batch().getColor();
			Game.batch().setColor(Color.RED);
			Game.batch().draw(Game.assets().get("debug.png", Texture.class), r1.x, r1.y, r1.width, r1.height);
			Game.batch().setColor(oldColor);
		}
	}

	@Override
	public void update(Entity entity) {
		for (Entity otherEntity : Game.entities().getEntities(EntityScope.MAP)) {
			// ensure it's another entity and it's on the same z axis
			if (otherEntity != entity && MathUtils.isEqual(entity.position.z, otherEntity.position.z, 2f)) {
				// only check a collision if both entities have an EntityCollisionComponent
				if (entity.getComponent(EntityCollisionComponent.class) != null && otherEntity.getComponent(EntityCollisionComponent.class) != null) {
					MapCollisionComponent component = entity.getComponent(MapCollisionComponent.class);
					MapCollisionComponent otherComponent = otherEntity.getComponent(MapCollisionComponent.class);
					// only check a collision if both entities have an MapCollisionComponent
					if (component != null && otherComponent != null) {
						r1.set(entity.position.x - component.getWidth() / 2, entity.position.y - component.getHeight() / 2, component.getWidth(), component.getHeight());
						r2.set(otherEntity.position.x - otherComponent.getWidth() / 2, otherEntity.position.y - otherComponent.getHeight() / 2, otherComponent.getWidth(), otherComponent.getHeight());
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
							// determine if we need to start an even triggered on touch
							final ScriptComponent scripts = otherEntity.getComponent(ScriptComponent.class);
							if (scripts != null) {
								scripts.setCollided();
							}
						}
					}
				}
			}
		}
	}

}
