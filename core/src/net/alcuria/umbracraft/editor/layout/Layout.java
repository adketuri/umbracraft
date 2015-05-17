package net.alcuria.umbracraft.editor.layout;

import net.alcuria.umbracraft.Config;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/** A top-level layout.
 * @author Andrew */
public abstract class Layout {
	protected Table content;
	protected Stage stage;

	public Layout() {
		stage = new Stage(new StretchViewport(Config.editorWidth, Config.editorHeight));
	}

	/** render the layout */
	public void render(SpriteBatch batch) {
		stage.draw();
	}

	/** resize the layout */
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	/** update the layout */
	public void update(float delta) {
		stage.act();
	}
}
