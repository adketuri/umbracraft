package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.config.ConfigDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** The EntityManager maintains all game entities and updates/renders them
 * accordingly. Entities are divided into three different {@link EntityScope}
 * types when added: <ol><li>MAP - the entity persists until the player changes
 * maps</li><li>AREA - the entity persists until the player changes
 * areas</li><li>GLOBAL - the entity sticks around FOREVER</li></ol>
 * @author Andrew Keturi */
public class EntityManager {

	/** The scope of entities found in the game. Area-specific, Map-specific, or
	 * global.
	 * @author Andrew Keturi */
	public static enum EntityScope {
		AREA, GLOBAL, MAP
	}

	private static final int ENTITY_TILE_PAD = 4;
	private final ObjectMap<EntityScope, Array<Entity>> entities = new ObjectMap<EntityScope, Array<Entity>>();
	private int mapHeight;
	private final Array<Entity> visibleEntities = new Array<Entity>();

	{
		entities.put(EntityScope.AREA, new Array<Entity>());
		entities.put(EntityScope.GLOBAL, new Array<Entity>());
		entities.put(EntityScope.MAP, new Array<Entity>());
	}

	/** Adds an entity to the manager of some particular scope.
	 * @param scope the {@link EntityScope}. for instance
	 *        {@link EntityScope#MAP} entities get cleared on map change.
	 * @param entity the {@link Entity} to add to the manager. */
	public void add(EntityScope scope, Entity entity) {
		if (scope == null) {
			throw new NullPointerException("Scope cannot be null for entity insertion");
		} else if (entity == null) {
			throw new NullPointerException("Entity cannot be null for entity insertion");
		}
		entities.get(scope).add(entity);
	}

	private void addComponents(Entity entity, String name) {
		EntityDefinition entityDef = Game.db().entity(name);
		if (entityDef != null) {
			for (ComponentDefinition componentDef : entityDef.components) {
				entity.addComponent(componentDef);
			}
			// TODO: for ControlledInputComponents, I think we need to set the input processor here...
			if (entity.getName().equals(Entity.PLAYER)) { //FIXME: ugleh
				Game.publisher().publish(new CameraTargetEvent(entity));
			}
		}
	}

	/** Takes as input the name of a map in the {@link Db} and creates all
	 * entities and their components for that map. Clears any existing entities.
	 * @param scope the scope of entities to create
	 * @param name the map id {@link String} */
	public void create(EntityScope scope, final String name) {
		if (scope == null) {
			throw new NullPointerException("scope cannot be null");
		}
		visibleEntities.clear();
		entities.get(scope).clear();
		if (scope == EntityScope.GLOBAL) {
			ConfigDefinition config = Game.db().config();
			if (config.globalEntities != null) {
				for (String entityName : config.globalEntities) {
					Entity entity = new Entity();
					entity.setName(entityName);
					entity.position.x = config.startingX * Config.tileWidth;
					entity.position.y = config.startingY * Config.tileWidth;
					addComponents(entity, entityName);
					entities.get(EntityScope.GLOBAL).add(entity);
				}
			}
		} else if (scope == EntityScope.AREA) {
			AreaDefinition area = Game.db().area(name);
			for (String entityName : area.entities) {
				Entity entity = new Entity(entityName);
				addComponents(entity, entity.getName());
				entities.get(EntityScope.AREA).add(entity);
			}
		} else if (scope == EntityScope.MAP) {
			// create entities
			MapDefinition mapDef = Game.db().map(name);
			mapHeight = mapDef.getHeight();
			if (mapDef != null && mapDef.entities != null) {
				for (EntityReferenceDefinition reference : mapDef.entities) {
					Entity entity = new Entity();
					entity.setFromReference(reference);
					addComponents(entity, reference.name);
					entities.get(EntityScope.MAP).add(entity);
				}
			} else {
				Game.error("Map not found: " + name);
			}
		}
	}

	/** Dispose all entities, freeing up any resources. */
	public void dispose(EntityScope scope) {
		if (scope == null) {
			throw new NullPointerException("Scope cannot be null");
		}
		if (entities == null) {
			return;
		}
		for (int i = 0; i < entities.get(scope).size; i++) {
			entities.get(scope).get(i).dispose();
		}
	}

	/** Finds an entity with the given name. Entities may have the same name, but
	 * the first one added to the {@link EntityManager} will be returned. If no
	 * entity is found with the given name, <code>null</code> is returned.
	 * @param name the name {@link String}
	 * @return the {@link Entity} with the given name, or <code>null</code> if
	 *         none is found */
	public Entity find(String name) {
		for (EntityScope scope : EntityScope.values()) {
			for (int i = 0; i < entities.get(scope).size; i++) {
				if (entities.get(scope).get(i).getName() != null && entities.get(scope).get(i).getName().equals(name)) {
					return entities.get(scope).get(i);
				}
			}
		}
		return null;
	}

	/** Gets the column an entity is at for rendering */
	private int getCol(Entity entity) {
		return (int) ((entity.position.x) / Config.tileWidth);
	}

	/** @param scope the {@link EntityScope} for which we want to fetch all
	 *        entities
	 * @return all entities in the {@link EntityManager}. This is not a copy so
	 *         be careful with it. */
	public Array<Entity> getEntities(EntityScope scope) {
		if (scope == null) {
			throw new NullPointerException("Entity scope cannot be null");
		}
		return entities.get(scope);
	}

	/** Gets the row an entity is at for rendering */
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
		for (EntityScope scope : EntityScope.values()) {
			for (int i = 0; i < entities.get(scope).size; i++) {
				final int entityRow = getRow(entities.get(scope).get(i));
				final int entityCol = getCol(entities.get(scope).get(i));
				if (entityCol > x && entityCol < x + width && entityRow < row && entityRow >= row - height && entityRow >= 0 && entityRow <= mapHeight) {
					visibleEntities.add(entities.get(scope).get(i));
				}
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
		Game.map().renderOverlays(x + ENTITY_TILE_PAD, y - ENTITY_TILE_PAD); // FIXME: required for the overlays since they're drawn 4 tiles up?
		visibleEntities.clear();
	}

	/** Attempts to render the path of any Entity with a
	 * {@link DirectedInputComponent} */
	public void renderPaths() {
		for (final Entity e : entities.get(EntityScope.MAP)) {
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
		for (EntityScope scope : EntityScope.values()) {
			for (int i = 0; i < entities.get(scope).size; i++) {
				entities.get(scope).get(i).update();
			}
		}
	}
}
