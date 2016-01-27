package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.engine.windows.WindowStack;

import com.badlogic.gdx.Screen;

/** An extension of {@link Screen} to separate out update calls into a different
 * method
 * @author Andrew Keturi */
public interface UmbraScreen extends Screen {

	WindowStack getWindows();

	/** Updates the screen. */
	void update(float delta);

}
