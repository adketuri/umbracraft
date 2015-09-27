package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.VisUI;

/** The engine.
 * @author Andrew Keturi */
public abstract class UmbraCraftApplication implements ApplicationListener {
	private Game game;

	@Override
	public void create() {
		VisUI.load();
		game = new Game();
		Game.setScreen(getFirstScreen());
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		game.dispose();
	}

	public abstract UmbraScreen getFirstScreen();

	@Override
	public void pause() {

	}

	@Override
	public void render() {
		if (Game.screen() != null) {
			Game.screen().update(Gdx.graphics.getDeltaTime());
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Game.batch().begin();
		Game.batch().setProjectionMatrix(Game.view().getCamera().combined);
		if (Game.screen() != null) {
			Game.screen().render(Gdx.graphics.getDeltaTime());
		}
		Game.batch().end();
	}

	@Override
	public void resize(int width, int height) {
		Game.view().resize(width, height);
	}

	@Override
	public void resume() {

	}

}
