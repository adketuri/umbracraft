package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.screens.UmbraScreen;
import net.alcuria.umbracraft.hud.HUD;

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
		Game.setScreen(getFirstScreen(), false);
		Game.setBattle(getBattle());
		Game.setHUD(getHUD());
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		game.dispose();
	}

	/** This method is called when the application is created to specify a
	 * {@link Battle} interface to tell the engine how to handle in-game
	 * battles. Once the application is created, a reference to this battle
	 * object is stored inside of {@link Game}.
	 * @return a {@link Battle} */
	public abstract Battle getBattle();

	/** @return the first screen to display, typically a loading or title screen. */
	public abstract UmbraScreen getFirstScreen();

	/** This method is called when the application is created to specify a
	 * {@link HUD} interface which tells the engine how to render and manage the
	 * in-game HUD. Once called, a reference is stored in {@link Game}
	 * @return */
	public abstract HUD getHUD();

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
