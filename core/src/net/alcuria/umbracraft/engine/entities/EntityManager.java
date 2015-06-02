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

	public EntityManager(Map map) {
		this.map = map;
		entities = new Array<Entity>();
		entities.add(EntityCreator.player(map));
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

	/** Renders all objects in view. */
	public void render() {
		if (entities == null) {
			return;
		}
		// get the visible tiles on the screen
		int x = (int) (Game.camera().getCamera().position.x - Config.viewWidth / 2) / Config.tileWidth;
		int y = (int) (Game.camera().getCamera().position.y - Config.viewHeight / 2) / Config.tileWidth;
		int width = Config.viewWidth / Config.tileWidth;
		int height = Config.viewHeight / Config.tileWidth;

		// render each row in view, starting from the top
		int row = y + height;
		int heroRow = (int) ((entities.get(0).position.y + entities.get(0).position.z) / Config.tileWidth);
		while (row > y - map.getMaxAltitude() * 2) {
			map.render(row);
			if (row + ((int) (entities.get(0).position.z / Config.tileWidth)) == heroRow) {
				entities.get(0).render();
			}
			row--;
		}
	}

	/** Updates the state of all objects. */
	public void update(float delta) {
		if (entities == null) {
			return;
		}
		map.update(delta);
		for (int i = 0; i < entities.size; i++) {
			entities.get(i).update(delta);
		}
	}
}
