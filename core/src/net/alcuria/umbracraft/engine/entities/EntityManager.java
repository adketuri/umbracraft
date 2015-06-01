package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.utils.Array;

/** The EntityManager maintains all game objects and updates/renders them
 * accordingly.
 * @author Andrew Keturi */
public class EntityManager {
	private final Array<Entity> gameObjects;
	private final Map map;

	public EntityManager(Map map) {
		this.map = map;
		gameObjects = new Array<Entity>();
		gameObjects.add(EntityCreator.player(map));
	}

	public void dispose() {
		if (gameObjects == null) {
			return;
		}
		map.dispose();
		for (int i = 0; i < gameObjects.size; i++) {
			gameObjects.get(i).dispose();
		}
	}

	/** Renders all objects in view. */
	public void render() {
		if (gameObjects == null) {
			return;
		}
		// get the visible tiles on the screen
		int x = (int) (Game.camera().getCamera().position.x - Config.viewWidth / 2) / Config.tileWidth;
		int y = (int) (Game.camera().getCamera().position.y - Config.viewHeight / 2) / Config.tileWidth;
		int width = Config.viewWidth / Config.tileWidth;
		int height = Config.viewHeight / Config.tileWidth;

		// render each row in view, starting from the top
		int row = y + height;
		int heroRow = (int) ((gameObjects.get(0).position.y + gameObjects.get(0).position.z) / Config.tileWidth);
		while (row > y - map.getMaxAltitude() * 2) {
			map.render(row);
			if (row + (gameObjects.get(0).position.z / Config.tileWidth) == heroRow) {
				gameObjects.get(0).render();
			}
			row--;
		}
	}

	/** Updates the state of all objects. */
	public void update(float delta) {
		if (gameObjects == null) {
			return;
		}
		map.update(delta);
		for (int i = 0; i < gameObjects.size; i++) {
			gameObjects.get(i).update(delta);
		}
	}
}
