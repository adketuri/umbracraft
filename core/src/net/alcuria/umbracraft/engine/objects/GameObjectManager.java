package net.alcuria.umbracraft.engine.objects;

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
		for (int i = 0; i < gameObjects.size; i++) {
			gameObjects.get(i).dispose();
		}
	}

	public void render() {
		if (gameObjects == null) {
			return;
		}
		for (int i = 0; i < gameObjects.size; i++) {
			gameObjects.get(i).render();
		}
	}

	public void update(float delta) {
		if (gameObjects == null) {
			return;
		}
		for (int i = 0; i < gameObjects.size; i++) {
			gameObjects.get(i).update(delta);
		}
	}
}
