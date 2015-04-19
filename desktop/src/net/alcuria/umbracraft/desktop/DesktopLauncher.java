package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.UmbraCraftEditor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 700;
		new LwjglApplication(new UmbraCraftEditor(), config);
	}
}
