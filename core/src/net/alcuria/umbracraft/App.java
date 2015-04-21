package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.events.EventPublisher;
import net.alcuria.umbracraft.engine.screens.UmbraScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class App {

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

	public static OrthographicCamera camera() {
		return camera.getCamera();
	}

	public static CameraManager cameraManager() {
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
		if (App.screen != null) {
			App.screen.hide();
		}
		App.screen = screen;
		if (App.screen != null) {
			App.screen.show();
			App.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	public App() {
		// initialize everything
		config = new Config();
		assets = new AssetManager();
		camera = new CameraManager(new OrthographicCamera(config.viewWwidth, config.viewHeight));
		batch = new SpriteBatch();
		publisher = new EventPublisher();
		// now subscribe
		App.publisher().addListener(camera);
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
		batch = null;
		System.gc();
	}
}
