package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.App;

import com.badlogic.gdx.graphics.Texture;

public class Loading implements UmbraScreen {

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

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		App.assets().load("tiles/debug.png", Texture.class);

	}

	@Override
	public void update(float delta) {
		if (App.assets().update()) {
			App.setScreen(new World());
		}
	}

}
