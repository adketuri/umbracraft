package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;

public class EntityCollisionComponent implements BaseComponent {

	private Array<Entity> entities;

	@Override
	public void create(Entity entity) {

	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {

	}

	/** Sets a reference of the entities.
	 * @param entities */
	public void setEntities(Array<Entity> entities) {
		this.entities = entities;
	}

	@Override
	public void update(Entity entity) {
		for (Entity otherEntity : entities) {
			if (otherEntity != entity) {
				PhysicsComponent component = entity.getComponent(PhysicsComponent.class);
				return;
			}
		}
	}

}
