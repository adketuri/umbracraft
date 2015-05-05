package net.alcuria.umbracraft.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** A top-level layout.
 * @author Andrew */
public abstract class Layout {
	protected Table content;
	protected Stage stage;

	/** render the layout */
	public void render(SpriteBatch batch) {
		stage.draw();
	}

	/** update the layout */
	public void update(float delta) {
		stage.act();
	}
}
