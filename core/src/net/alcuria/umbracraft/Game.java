package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game {

	private static AssetManager assets;
	private static SpriteBatch batch;
	private static CameraManager camera;
	private static Config config;
	private static EventPublisher publisher;
	private static UmbraScreen screen;

	public static AssetManager assets() {
		return assets;
	}

	public static SpriteBatch batch() {
		return batch;
	}

	public static CameraManager camera() {
		return camera;
	}

	public static Config config() {
		return config;
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
		config = new Config();
		assets = new AssetManager();
		camera = new CameraManager(new OrthographicCamera(config.viewWidth, config.viewHeight));
		batch = new SpriteBatch();
		publisher = new EventPublisher();
		// now subscribe
		Game.publisher().addListener(camera);
	}

	public void dispose() {
		config = null;
		if (assets != null) {
			assets.dispose();
		}
		assets = null;
		if (batch != null) {
			batch.dispose();
		}
		camera = null;
		config = null;
		if (publisher != null) {
			publisher.removeAllListeners();
		}
		publisher = null;
		if (screen != null) {
			screen.dispose();
		}
		screen = null;
		System.gc();
	}
}
