package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.audio.Audio;
import net.alcuria.umbracraft.engine.entities.EntityManager;
import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.inventory.Inventory;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;
import net.alcuria.umbracraft.engine.windows.WindowStack;
import net.alcuria.umbracraft.flags.FlagManager;
import net.alcuria.umbracraft.hud.HUD;
import net.alcuria.umbracraft.party.Party;
import net.alcuria.umbracraft.save.DiskSaveManager;
import net.alcuria.umbracraft.save.model.GameStatsManager;
import net.alcuria.umbracraft.save.model.Saveable;
import net.alcuria.umbracraft.variables.VariableManager;
import net.sourceforge.jeval.Evaluator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Contains everything needed globally by the Engine. These should be modified
 * with caution.
 * @author Andrew Keturi */
public final class Game {

	private static AreaBuilder areas;
	private static AssetManager assets;
	private static Audio audio;
	private static SpriteBatch batch;
	private static Battle battle;
	private static Db db;
	private static boolean debug = false;
	private static EntityManager entities;
	private static final Evaluator eval = new Evaluator();
	private static FlagManager flags;
	private static HUD hud;
	private static Inventory inventory;
	private static Map map;
	private static Party party;
	private static EventPublisher publisher;
	private static Saveable save;
	private static UmbraScreen screen;
	private static GameStatsManager stats;
	private static VariableManager variables;
	private static View view;

	/** @return the {@link AreaBuilder} */
	public static AreaBuilder areas() {
		return areas;
	}

	/** @return the {@link AssetManager} */
	public static AssetManager assets() {
		return assets;
	}

	/** @return the {@link Audio} */
	public static Audio audio() {
		return audio;
	}

	/** @return the {@link SpriteBatch} used to render everything */
	public static SpriteBatch batch() {
		return batch;
	}

	/** @return the {@link Battle} used to trigger battles. The battle must be
	 *         initialized and set first, otherwise an exception will be thrown. */
	public static Battle battle() {
		if (battle == null) {
			throw new NullPointerException("Battle has not been initialized");
		}
		return battle;
	}

	/** @return the {@link Db} */
	public static Db db() {
		return db;
	}

	/** Logs only if we're in debug mode
	 * @param string the debug message to print to console */
	public static void debug(String string) {
		if (debug) {
			log(string);
		}
	}

	/** @return the {@link EntityManager} consisting of all of the entities */
	public static EntityManager entities() {
		return entities;
	}

	/** Prints an error message to stdout
	 * @param string the message. */
	public static void error(String string) {
		System.err.println(string);
		stack(2);
	}

	/** @return the {@link Evaluator} */
	public static Evaluator eval() {
		return eval;
	}

	/** @return the {@link FlagManager} */
	public static FlagManager flags() {
		return flags;
	}

	/** @return the {@link HUD} */
	public static HUD hud() {
		return hud;
	}

	/** @return whether or not the game is in debug mode. */
	public static boolean isDebug() {
		return debug;
	}

	/** @return the game {@link Inventory} */
	public static Inventory items() {
		return inventory;
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

	/** @return the current {@link Party} */
	public static Party party() {
		return party;
	}

	/** @return The {@link EventPublisher} for subscribing and and publishing
	 *         events */
	public static EventPublisher publisher() {
		return publisher;
	}

	/** @return the {@link Saveable} manager to handle saving game progress */
	public static Saveable save() {
		return save;
	}

	/** @return the current {@link UmbraScreen} */
	public static UmbraScreen screen() {
		return screen;
	}

	/** Sets the audio type */
	public static void setAudio(Audio audio) {
		Game.audio = audio;
	}

	/** Sets the battle type */
	public static void setBattle(Battle battle) {
		Game.battle = battle;
	}

	/** Sets whether or not the game is in debug mode
	 * @param debug is the game in debug mode? */
	public static void setDebug(boolean debug) {
		Game.debug = debug;
	}

	/** Sets the hud type */
	public static void setHUD(HUD hud) {
		Game.hud = hud;
	}

	/** Changes the current screen
	 * @param newScreen the {@link UmbraScreen}
	 * @param dispose if <code>true</code>, dispose after hiding old screen */
	public static void setScreen(UmbraScreen newScreen, boolean dispose) {
		if (screen != null) {
			screen.hide();
			if (dispose) {
				screen.dispose();
			}
		}
		screen = newScreen;
		if (screen != null) {
			screen.show();
			screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	/** Plays a sound effect
	 * @param sound a sound asset location */
	public static void sound(String sound) {
		if (audio != null && audio.manager != null) {
			audio.manager.sound(sound);
		}
	}

	/** Prints the stack */
	public static void stack() {
		stack(1);
	}

	/** Prints the stack, starts at a certain index */
	private static void stack(int index) {
		final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		for (int i = index; i < stackTrace.length; i++) {
			System.out.println(stackTrace[i].toString());
		}
	}

	/** @return the game stats */
	public static GameStatsManager stats() {
		return stats;
	}

	/** @return the {@link VariableManager} */
	public static VariableManager variables() {
		return variables;
	}

	/** @return the {@link View} for handling cameras and so on. */
	public static View view() {
		return view;
	}

	/** @return the screen's current windows */
	public static WindowStack windows() {
		return screen.getWindows();
	}

	public Game() {
		// initialize everything. Order matters (iirc).
		assets = new AssetManager();
		view = new View();
		batch = new SpriteBatch();
		db = new Db();
		entities = new EntityManager();
		map = new Map();
		party = new Party();
		publisher = new EventPublisher();
		areas = new AreaBuilder();
		flags = new FlagManager();
		variables = new VariableManager();
		save = new DiskSaveManager();
		stats = new GameStatsManager();
		inventory = new Inventory();
		// now subscribe
		publisher.subscribe(view);
	}

	/** Destroys everything */
	public void dispose() {
		if (assets != null) {
			assets.dispose();
		}
		assets = null;
		if (batch != null) {
			batch.dispose();
		}
		if (publisher != null) {
			publisher.removeAllListeners();
		}
		if (screen != null) {
			screen.dispose();
		}
		if (areas != null) {
			areas.dispose();
		}
		if (flags != null) {
			flags.dispose();
		}
		if (variables != null) {
			variables.dispose();
		}
		stats = null;
		inventory = null;
		save.dispose();
		publisher.removeAllListeners();
		publisher = null;
		db = null;
		screen = null;
		System.gc();
	}
}
