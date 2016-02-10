package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;

import com.badlogic.gdx.utils.Array;

/** The EntityManager maintains all game objects and updates/renders them
 * accordingly.
 * @author Andrew Keturi */
public class EntityManager {
	private static final int ENTITY_TILE_PAD = 4;
	private final Array<Entity> entities = new Array<Entity>();
	private int mapHeight;
	private final Array<Entity> visibleEntities = new Array<Entity>();

	/** Adds an entity to the manager. If create is called afterwards this entity
	 * will probably be gone.
	 * @param entity the {@link Entity} */
	public void add(Entity entity) {
		entities.add(entity);
	}

	/** Takes as input the name of a map in the {@link Db} and creates all
	 * entities and their components for that map. Clears any existing entities.
	 * @param mapName the map id {@link String} */
	public void create(final String mapName) {
		visibleEntities.clear();
		entities.clear();
		// create entities
		MapDefinition mapDef = Game.db().map(mapName);
		mapHeight = mapDef.getHeight();
		if (mapDef != null && mapDef.entities != null) {
			for (EntityReferenceDefinition reference : mapDef.entities) {
				Entity entity = new Entity();
				entity.setName(reference.name);
				entity.position.x = reference.x * Config.tileWidth;
				entity.position.y = reference.y * Config.tileWidth;
				EntityDefinition entityDef = Game.db().entity(reference.name);
				if (entityDef != null) {
					for (ComponentDefinition componentDef : entityDef.components) {
						entity.addComponent(componentDef);
					}
					// TODO: for ControlledInputComponents, I think we need to set the input processor here...
					if (entity.getName().equals(Entity.PLAYER)) { //FIXME: ugleh
						Game.publisher().publish(new CameraTargetEvent(entity));
					}
					entities.add(entity);
				}
			}
		} else {
			Game.error("Map not found: " + mapName);
		}
	}

	/** Dispose all entities, freeing up any resources. */
	public void dispose() {
		if (entities == null) {
			return;
		}
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).dispose();
		}
	}

	/** Finds an entity with the given name. Entities may have the same name, but
	 * the first one added to the {@link EntityManager} will be returned. If no
	 * entity is found with the given name, <code>null</code> is returned.
	 * @param name the name {@link String}
	 * @return the {@link Entity} with the given name, or <code>null</code> if
	 *         none is found */
	public Entity find(String name) {
		for (Entity entity : entities) {
			if (entity.getName() != null && entity.getName().equals(name)) {
				return entity;
			}
		}
		return null;
	}

	/** gets the column an entity is at for rendering */
	private int getCol(Entity entity) {
		return (int) ((entity.position.x) / Config.tileWidth);
	}

	/** @return all entities in the {@link EntityManager} */
	public Array<Entity> getEntities() {
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
		int x = (int) (Game.view().getCamera().position.x - Config.viewWidth / 2) / Config.tileWidth - ENTITY_TILE_PAD;
		int y = (int) (Game.view().getCamera().position.y - Config.viewHeight / 2) / Config.tileWidth - ENTITY_TILE_PAD;
		int width = Config.viewWidth / Config.tileWidth + ENTITY_TILE_PAD * 2;
		int height = (Config.viewHeight / Config.tileWidth) + ENTITY_TILE_PAD * 2;

		// add visible entitities onscreen
		int row = y + height; // start at the top
		for (int i = 0; i < entities.size; i++) {
			final int entityRow = getRow(entities.get(i));
			final int entityCol = getCol(entities.get(i));
			if (entityCol > x && entityCol < x + width && entityRow < row && entityRow >= row - height && entityRow >= 0 && entityRow <= mapHeight) {
				visibleEntities.add(entities.get(i));
			}
		}
		visibleEntities.sort();

		// render each row in view, starting from the top
		int idx = 0;
		while (row > y - Game.map().getMaxAltitude() * 2) {
			Game.map().render(row, x + ENTITY_TILE_PAD);
			while (idx < visibleEntities.size && (visibleEntities.get(idx).position.y - 4) / Config.tileWidth >= row) {
				visibleEntities.get(idx).render();
				idx++;
			}
			row--;
		}

		// render our overlays last
		Game.map().renderOverlays(x + ENTITY_TILE_PAD, y - 4); // FIXME: required for the overlays since they're drawn 4 tiles up?
		visibleEntities.clear();
	}

	/** Attempts to render the path of any Entity with a
	 * {@link DirectedInputComponent} */
	public void renderPaths() {
		for (final Entity e : entities) {
			final DirectedInputComponent component = e.getComponent(DirectedInputComponent.class);
			if (component != null) {
				component.renderPaths();
			}
		}

	}

	/** Sets the height of the map, for rendering. This should be in pixels.
	 * @param height height of the map */
	public void setRenderHeight(int height) {
		mapHeight = height;
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
