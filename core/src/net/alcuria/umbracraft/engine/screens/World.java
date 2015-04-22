package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObjectManager;

public class World implements UmbraScreen {
	private Map map;
	private GameObjectManager objects;

	@Override
	public void dispose() {
		objects.dispose();
		map.dispose();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		map.render();
		objects.render();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		map = new Map();
		objects = new GameObjectManager(map);
	}

	@Override
	public void update(float delta) {
		map.update(delta);
		objects.update(delta);
		Game.camera().update();
	}
}
