package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObjectManager;

public class World implements UmbraScreen {
	private GameObjectManager objects;

	@Override
	public void dispose() {
		objects.dispose();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
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
		Map map = new Map();
		objects = new GameObjectManager(map);
	}

	@Override
	public void update(float delta) {
		objects.update(delta);
		Game.camera().update();
	}
}
