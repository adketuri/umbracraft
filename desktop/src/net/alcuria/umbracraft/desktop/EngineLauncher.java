package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.Config;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/** Launches the game engine
 * @author Andrew Keturi */
public class EngineLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.viewWidth;
		config.height = Config.viewHeight;
		throw new UnsupportedOperationException();
	}
}
