package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.engine.components.GameObjectManager;
import net.alcuria.umbracraft.engine.map.Map;

public class World implements UmbraScreen {
	private Map map;
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
		objects = new GameObjectManager();
	}

	@Override
	public void update(float delta) {
		map.update(delta);
		objects.update(delta);
		App.camera().update();
	}

}
