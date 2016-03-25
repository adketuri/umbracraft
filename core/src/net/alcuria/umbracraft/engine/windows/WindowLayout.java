package net.alcuria.umbracraft.engine.windows;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.listeners.TypeListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** A layout for a {@link Window}. Contains the contents {@link Table} inside of
 * a {@link Stage} and callbacks for the implementing subclasses when windows
 * are created/destroyed and when a key is pressed.
 * @author Andrew Keturi */
public abstract class WindowLayout extends InputAdapter {
	protected Table content;
	private boolean isActive;
	private boolean isTouching;
	protected Table root;
	protected Stage stage;
	private TypeListener<InputCode> typeListener;

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

	/** Called to hide/close the window. The listener must be called after any
	 * animations. */
	public abstract void hide(Listener completeListener);

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE && typeListener != null) {
			Game.log("just pressed");
			typeListener.invoke(InputCode.CANCEL);
			return true;
		}
		if (keycode == Keys.ENTER && typeListener != null) {
			typeListener.invoke(InputCode.CONFIRM);
			return true;
		}
		return false;
	}

	/** Draws the stage. */
	public void render() {
		stage.draw();
	}

	/** Sets whether or not the window is active and accepting input. Note, this
	 * is false for the first frame the window is created then set to true. This
	 * fixes an issue where inputs are getting consumed twice due to not
	 * actually using an InputProcessor and relying on Gdx.isKeyJustPressed.
	 * It's a limitation and sort of a hack but it works for the time.
	 * @param isActive */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setTypeListener(TypeListener<InputCode> typeListener) {
		this.typeListener = typeListener;
	}

	/** Called to create/show the window. */
	public abstract void show();

	/** Updates the stage and checks for any input. */
	public void update() {
		stage.act();
		// invoke ontouch if we press enter
		// invoke on touch only once
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!isTouching) {
				if (typeListener != null) {
					typeListener.invoke(InputCode.CONFIRM);
				}
				isTouching = true;
			}
		} else {
			isTouching = false;
		}

	}

}
