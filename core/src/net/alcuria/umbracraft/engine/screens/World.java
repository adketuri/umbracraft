package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObjectManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

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
		objects = new GameObjectManager(map);
	}

	@Override
	public void update(float delta) {
		map.update(delta);
		if (Gdx.input.isKeyPressed(Keys.W)) {
			App.camera().translate(0, 4, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			App.camera().translate(-4, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			App.camera().translate(0, -4, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			App.camera().translate(4, 0, 0);
		}
		objects.update(delta);
		App.cameraManager().update();
		App.camera().update();
	}
}
