package net.alcuria.umbracraft.engine.objects;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.utils.Array;

/** The GameObjectManager maintains all game objects and updates/renders them
 * accordingly.
 * @author Andrew Keturi */
public class GameObjectManager {
	private final Array<GameObject> gameObjects;
	private final Map map;

	public GameObjectManager(Map map) {
		this.map = map;
		gameObjects = new Array<GameObject>();
		gameObjects.add(GameObjectCreator.player(map));
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

	public void render() {
		if (gameObjects == null) {
			return;
		}
		// get the visible tiles on the screen
		int x = (int) (Game.camera().getCamera().position.x - Game.config().viewWwidth / 2) / Game.config().tileWidth;
		int y = (int) (Game.camera().getCamera().position.y - Game.config().viewHeight / 2) / Game.config().tileWidth;
		int width = (int) (Game.config().viewWwidth / Game.config().tileWidth);
		int height = (int) (Game.config().viewHeight / Game.config().tileWidth);

		// render each row in view, starting from the top
		int row = y + height;
		int heroRow = (int) ((gameObjects.get(0).position.y) / Game.config().tileWidth);
		Game.log("" + map.getMaxAltitude());
		while (row > y - map.getMaxAltitude() * 2) {
			map.render(row);
			if (row + gameObjects.get(0).altitude / 16 == heroRow) {
				gameObjects.get(0).render();
			}
			row--;

		}

	}

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
