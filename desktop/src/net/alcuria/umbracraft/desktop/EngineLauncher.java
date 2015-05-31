package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.UmbraCraftEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/** Launches the game engine
 * @author Andrew Keturi */
public class EngineLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Config.viewWidth;
		config.height = Config.viewHeight;
		new LwjglApplication(new UmbraCraftEngine(), config);
	}
}
