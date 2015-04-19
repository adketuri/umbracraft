package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.screens.Loading;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.VisUI;

public class UmbraCraftEngine implements ApplicationListener {
	private App app;

	@Override
	public void create() {
		VisUI.load();
		app = new App();
		App.setScreen(new Loading());
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		app.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		if (App.screen() != null) {
			App.screen().update(Gdx.graphics.getDeltaTime());
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		App.batch().begin();
		App.batch().setProjectionMatrix(App.camera().combined);
		if (App.screen() != null) {
			App.screen().render(Gdx.graphics.getDeltaTime());
		}
		App.batch().end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

}
