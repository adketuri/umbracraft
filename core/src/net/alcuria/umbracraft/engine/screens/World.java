package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.graphics.Texture;

public class World implements UmbraScreen {
	private Map map;
	private Texture tex;

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		App.batch().draw(tex, 0, 0);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		tex = App.assets().get("tiles/debug.png", Texture.class);
	}

	@Override
	public void update(float delta) {
		App.camera().rotate(delta);
		App.camera().update();
	}

}
