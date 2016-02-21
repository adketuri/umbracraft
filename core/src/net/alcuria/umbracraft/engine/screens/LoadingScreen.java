package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;

/** A screen to load all assets.
 * @author Andrew Keturi */
public abstract class LoadingScreen extends UmbraScreen {

	@Override
	public void dispose() {

	}

	/** @return the {@link UmbraScreen} to display after the game has loaded. */
	public abstract UmbraScreen getNextScreen();

	@Override
	public void hide() {

	}

	/** Queues up all assets to be loaded. These should be listed
	 * Game.assets().load(...) statements. */
	public abstract void loadAssets();

	@Override
	public void pause() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		loadAssets();
	}

	@Override
	public void onUpdate(float delta) {
		if (Game.assets().update()) {
			Game.setScreen(getNextScreen(), true);
		}
	}

}
