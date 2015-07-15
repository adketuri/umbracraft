package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Config;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/** A layout for a {@link Window}
 * @author Andrew Keturi */
public abstract class WindowLayout {
	protected Table content;
	protected Table root;
	protected Stage stage;

	public WindowLayout() {
		content = new Table();
		root = new Table();
		root.setFillParent(true);
		stage = new Stage(new StretchViewport(Config.viewWidth, Config.viewHeight));
		stage.addActor(root);
		root.add(content).expand().fill();
		create();
	}

	/** override this to create the layout */
	public abstract void create();

	/** draws the stage */
	public void render() {
		stage.draw();
	}

	/** updates the stage */
	public void update() {
		stage.act();
	}
}
