package net.alcuria.umbracraft.desktop;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.UmbraCraftEditor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/** Launches the game editor.
 * @author Andrew Keturi */
public class EditorLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Config.editorWidth;
		config.height = Config.editorHeight;
		new LwjglApplication(new UmbraCraftEditor(), config);
	}
}
