package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Contains everything needed by the Engine.
 * @author Andrew Keturi */
public class Game {

	private static AssetManager assets;
	private static SpriteBatch batch;
	private static View view;
	private static Db db;
	private static EventPublisher publisher;
	private static UmbraScreen screen;

	public static AssetManager assets() {
		return assets;
	}

	public static SpriteBatch batch() {
		return batch;
	}

	public static View view() {
		return view;
	}

	public static Db db() {
		return db;
	}

	public static void log(String string) {
		System.out.println(string);
	}

	public static EventPublisher publisher() {
		return publisher;
	}

	public static UmbraScreen screen() {
		return screen;
	}

	public static void setScreen(UmbraScreen screen) {
		if (Game.screen != null) {
			Game.screen.hide();
		}
		Game.screen = screen;
		if (Game.screen != null) {
			Game.screen.show();
			Game.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	public Game() {
		// initialize everything
		assets = new AssetManager();
		view = new View();
		batch = new SpriteBatch();
		db = new Db();
		publisher = new EventPublisher();
		// now subscribe
		publisher.addListener(view);
	}

	public void dispose() {
		if (assets != null) {
			assets.dispose();
		}
		assets = null;
		if (batch != null) {
			batch.dispose();
		}
		view = null;
		if (publisher != null) {
			publisher.removeAllListeners();
		}
		publisher.removeAllListeners();
		publisher = null;
		if (screen != null) {
			screen.dispose();
		}
		db = null;
		screen = null;
		System.gc();
	}
}
