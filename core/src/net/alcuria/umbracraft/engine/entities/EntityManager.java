package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.utils.Array;

/** The EntityManager maintains all game objects and updates/renders them
 * accordingly.
 * @author Andrew Keturi */
public class EntityManager {
	private final Array<Entity> entities;
	private final Map map;
	private final Array<Entity> visibleEntities;

	public EntityManager(Map map) {
		this.map = map;
		visibleEntities = new Array<Entity>();
		entities = new Array<Entity>();
		entities.add(EntityCreator.player(map));
		for (int i = 0; i < 5; i++) {
			entities.add(EntityCreator.dummy(map));
		}
		entities.add(EntityCreator.event(map));
	}

	public void dispose() {
		if (entities == null) {
			return;
		}
		map.dispose();
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).dispose();
		}
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
			if (entityRow < row && entityRow >= row - height && entityRow >= 0 && entityRow <= map.getHeight()) {
				visibleEntities.add(entities.get(i));
				added++;
			}
		}
		visibleEntities.sort();
		for (int i = 0; i < visibleEntities.size; i++) {
			//			Game.log(visibleEntities.get(i).position.y + "");
		}
		// render each row in view, starting from the top
		int rendered = 0;
		int idx = 0;
		while (row > y - map.getMaxAltitude() * 2) {
			map.render(row, x);
			while (idx < visibleEntities.size && visibleEntities.get(idx).position.y / Config.tileWidth >= row) {
				visibleEntities.get(idx).render();
				idx++;
				rendered++;
			}
			row--;
		}
		//		Game.log("entities to render: " + added + " rendered: " + rendered);
		visibleEntities.clear();
	}

	/** Updates the state of all objects. */
	public void update(float delta) {
		if (entities == null) {
			return;
		}
		map.update(delta);
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).update();
		}
	}
}
