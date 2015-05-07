package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.UmbraCraftEditor;
import net.alcuria.umbracraft.UmbraCraftEngine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		boolean editor = true;
		if (editor) {
			config.width = Config.editorWidth;
			config.height = Config.editorHeight;
			new LwjglApplication(new UmbraCraftEditor(), config);
		} else {
			config.width = Config.viewWidth;
			config.height = Config.viewHeight;
			new LwjglApplication(new UmbraCraftEngine(), config);
		}

	}
}
