package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.entities.EntityManager;
import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Contains everything needed by the Engine.
 * @author Andrew Keturi */
public final class Game {

	private static AreaBuilder areas;
	private static AssetManager assets;
	private static SpriteBatch batch;
	private static Db db;
	private static boolean debug = false;
	private static EntityManager entities;
	private static Map map;
	private static EventPublisher publisher;
	private static UmbraScreen screen;
	private static View view;

	public static AreaBuilder areas() {
		return areas;
	}

	/** @return the {@link AssetManager} */
	public static AssetManager assets() {
		return assets;
	}

	/** @return the {@link SpriteBatch} used to render everything */
	public static SpriteBatch batch() {
		return batch;
	}

	/** @return the {@link Db} */
	public static Db db() {
		return db;
	}

	/** @return the {@link EntityManager} consisting of all of the entities */
	public static EntityManager entities() {
		return entities;
	}

	/** Prints an error message to stdout
	 * @param string the message. */
	public static void error(String string) {
		System.err.println(string);
	}

	/** @return whether or not the game is in debug mode. */
	public static boolean isDebug() {
		return debug;
	}

	/** Prints to stdout
	 * @param string the {@link String} to print */
	public static void log(String string) {
		System.out.println(string);
	}

	/** @return the current active {@link Map} */
	public static Map map() {
		return map;
	}

	/** @return The {@link EventPublisher} for subscribing and and publishing
	 *         events */
	public static EventPublisher publisher() {
		return publisher;
	}

	/** @return the current {@link UmbraScreen} */
	public static UmbraScreen screen() {
		return screen;
	}

	/** Sets whether or not the game is in debug mode
	 * @param debug is the game in debug mode? */
	public static void setDebug(boolean debug) {
		Game.debug = debug;
	}

	/** Changes the current screen
	 * @param screen the {@link UmbraScreen} */
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

	/** @return the {@link View} for handling cameras and so on. */
	public static View view() {
		return view;
	}

	public Game() {
		// initialize everything
		assets = new AssetManager();
		view = new View();
		batch = new SpriteBatch();
		db = new Db();
		entities = new EntityManager();
		map = new Map();
		publisher = new EventPublisher();
		areas = new AreaBuilder();
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
		if (areas != null) {
			areas.dispose();
		}
		publisher.removeAllListeners();
		publisher = null;
		db = null;
		screen = null;
		System.gc();
	}
}
