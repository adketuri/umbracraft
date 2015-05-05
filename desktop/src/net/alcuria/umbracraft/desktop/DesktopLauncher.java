package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.UmbraCraftEditor;
import net.alcuria.umbracraft.UmbraCraftEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		boolean editor = false;
		if (editor) {
			config.width = 1200;
			config.height = 780;
			new LwjglApplication(new UmbraCraftEditor(), config);
		} else {
			config.width = 640;
			config.height = 480;
			new LwjglApplication(new UmbraCraftEngine(), config);
		}

	}
}
