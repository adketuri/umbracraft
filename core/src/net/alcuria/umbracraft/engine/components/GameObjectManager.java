package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.engine.components.base.GameObject;

import com.badlogic.gdx.utils.Array;

/**
 * The GameObjectManager maintains all game objects and updates/renders them accordingly.
 * @author Andrew Keturi
 *
 */
public class GameObjectManager {
	private final Array<GameObject> gameObjects;

	public GameObjectManager() {
		gameObjects = new Array<GameObject>();
		gameObjects.add(GameObjectCreator.player());
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
