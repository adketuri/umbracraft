package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
		stage = new Stage(new FitViewport(Config.viewWidth, Config.viewHeight));
		stage.addActor(root);
		root.add(content).expand().fill();
	}

	/** @return the stage */
	public InputProcessor getStage() {
		return stage;
	}

	/** override to hide/close the window. listener must be called after any
	 * animations. */
	public abstract void hide(Listener completeListener);

	/** draws the stage */
	public void render() {
		stage.draw();
	}

	/** override this to create/show the window. */
	public abstract void show();

	/** updates the stage */
	public void update() {
		stage.act();
	}
}
