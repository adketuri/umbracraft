package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.entities.EntityManager;
import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;
import net.alcuria.umbracraft.engine.windows.WindowStack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Contains everything needed by the Engine.
 * @author Andrew Keturi */
public class Game {

	private static AssetManager assets;
	private static SpriteBatch batch;
	private static Db db;
	private static boolean debug = false;
	private static EntityManager entities;
	private static EventPublisher publisher;
	private static UmbraScreen screen;
	private static View view;
	private static WindowStack windows;

	public static AssetManager assets() {
		return assets;
	}

	public static SpriteBatch batch() {
		return batch;
	}

	public static Db db() {
		return db;
	}

	public static EntityManager entities() {
		return entities;
	}

	public static boolean isDebug() {
		return debug;
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

	public static void setDebug(boolean debug) {
		Game.debug = debug;
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

	public static View view() {
		return view;
	}

	public static WindowStack windows() {
		return windows;
	}

	public Game() {
		// initialize everything
		assets = new AssetManager();
		view = new View();
		batch = new SpriteBatch();
		db = new Db();
		entities = new EntityManager();
		windows = new WindowStack();
		publisher = new EventPublisher();
		// now subscribe
		publisher.subscribe(view);
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
		if (screen != null) {
			screen.dispose();
		}
		windows.dispose();
		publisher.removeAllListeners();
		publisher = null;
		db = null;
		screen = null;
		System.gc();
	}
}
