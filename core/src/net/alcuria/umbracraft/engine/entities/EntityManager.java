package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;

import com.badlogic.gdx.utils.Array;

/** The EntityManager maintains all game objects and updates/renders them
 * accordingly.
 * @author Andrew Keturi */
public class EntityManager {
	private final Array<Entity> entities = new Array<Entity>();
	private final Array<Entity> visibleEntities = new Array<Entity>();

	public void create() {
		visibleEntities.clear();
		entities.clear();
		// create entities
		MapDefinition mapDef = Game.db().map("Andrew");
		if (mapDef != null && mapDef.entities != null) {
			for (EntityReferenceDefinition reference : mapDef.entities) {
				try {
					Entity entity = new Entity();
					entity.setName(reference.name);
					entity.position.x = reference.x * Config.tileWidth;
					entity.position.y = reference.y * Config.tileWidth;
					EntityDefinition entityDef = Game.db().entity(reference.name);
					if (entityDef != null) {
						for (ComponentDefinition componentDef : entityDef.components) {
							entity.addComponent(componentDef);
						}
					}
					if (entity.getName().equals(Entity.PLAYER)) { //FIXME: ugleh
						Game.publisher().publish(new CameraTargetEvent(entity));
					}
					entities.add(entity);
				} catch (Exception e) {

				}
			}
		}
	}

	public void dispose() {
		if (entities == null) {
			return;
		}
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).dispose();
		}
	}

	public Entity find(String name) {
		for (Entity entity : entities) {
			if (entity.getName() != null && entity.getName().equals(name)) {
				return entity;
			}
		}
		return null;
	}

	public Array<Entity> get() {
		return entities;
	}

	/** gets the row an entity is at for rendering */
	private int getRow(Entity entity) {
		return (int) ((entity.position.y + entity.position.z) / Config.tileWidth);
	}

	/** Renders all objects in view. */
	public void render() {
		if (entities == null) {
			return;
		}
		// get the visible tiles on the screen
		int x = (int) (Game.view().getCamera().position.x - Config.viewWidth / 2) / Config.tileWidth;
		int y = (int) (Game.view().getCamera().position.y - Config.viewHeight / 2) / Config.tileWidth;
		int width = Config.viewWidth / Config.tileWidth;
		int height = Config.viewHeight / Config.tileWidth;
		// add visible entitities onscreen
		int added = 0;
		int row = y + height;
		for (int i = 0; i < entities.size; i++) {
			final int entityRow = getRow(entities.get(i));
			if (entityRow < row && entityRow >= row - height && entityRow >= 0 && entityRow <= Game.map().getHeight()) {
				visibleEntities.add(entities.get(i));
				added++;
			}
		}
		visibleEntities.sort();
		// render each row in view, starting from the top
		int idx = 0;
		while (row > y - Game.map().getMaxAltitude() * 2) {
			Game.map().render(row, x);
			while (idx < visibleEntities.size && visibleEntities.get(idx).position.y / Config.tileWidth >= row) {
				visibleEntities.get(idx).render();
				idx++;
			}
			row--;
		}
		visibleEntities.clear();
	}

	/** Updates the state of all objects. */
	public void update(float delta) {
		if (entities == null) {
			return;
		}
		Game.map().update(delta);
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).update();
		}
	}
}
