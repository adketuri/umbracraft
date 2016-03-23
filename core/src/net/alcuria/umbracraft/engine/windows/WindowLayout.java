package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.WindowListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** A layout for a {@link Window}. Contains the contents {@link Table}.
 * @author Andrew Keturi */
public abstract class WindowLayout {
	protected Table content;
	private boolean isTouching;
	protected Table root;
	protected Stage stage;
	protected WindowListener windowListener;

	public WindowLayout() {
		content = new Table();
		root = new Table();
		root.setFillParent(true);
		stage = new Stage(new FitViewport(Config.viewWidth, Config.viewHeight));
		stage.addActor(root);
		root.add(content).expand().fill();
		content.setDebug(Game.isDebug(), true);
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

	/** Sets a listener to invoke on-touch
	 * @param onTouch */
	public void setTouchListener(WindowListener windowListener) {
		this.windowListener = windowListener;
	}

	/** override this to create/show the window. */
	public abstract void show();

	/** updates the stage */
	public void update() {
		stage.act();
		// invoke ontouch if we press enter
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) && windowListener != null) {
			windowListener.onConfirm();
		}
		// invoke on touch only once
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!isTouching) {
				if (windowListener != null) {
					windowListener.onConfirm();
				}
				isTouching = true;
			}
		} else {
			isTouching = false;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			windowListener.onCancel();
		}
	}

}
