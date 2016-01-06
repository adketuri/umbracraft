package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.events.TouchpadCreatedEvent;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** An entity for handling a {@link Touchpad} to provide mobile input.
 * @author Andrew Keturi */
public class TouchpadEntity extends OnscreenInput implements InputProcessor {

	private static final int SIZE = 60;
	private int pointer = -1;
	private final Vector3 touch = new Vector3();
	private final Touchpad touchpad;

	public TouchpadEntity(Stage stage) {
		TouchpadStyle style = new TouchpadStyle();
		style.background = new TextureRegionDrawable(Drawables.skin("ui/joyBack"));
		style.knob = new TextureRegionDrawable(Drawables.skin("ui/joyNub"));
		touchpad = new Touchpad(3, style);
		touchpad.setBounds(40, 20, SIZE, SIZE);
		touchpad.getColor().a = 0;
		stage.addActor(touchpad);
		Game.publisher().publish(new TouchpadCreatedEvent(touchpad));
	}

	public Actor getActor() {
		return touchpad;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public void render() {
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touch.x = screenX;
		touch.y = screenY;
		touch.z = 0;
		this.pointer = pointer;
		Game.view().getViewport().unproject(touch);
		if (touch.x < Config.viewWidth * 0.6f && touch.y < Config.viewHeight * 0.6f) {
			touchpad.setBounds(touch.x - SIZE / 2, touch.y - SIZE / 2, 60, 60);
			touchpad.addAction(Actions.alpha(0.5f, 0.2f, Interpolation.pow2Out));
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touch.x = screenX;
		touch.y = screenY;
		touch.z = 0;
		Game.view().getViewport().unproject(touch);
		if (this.pointer != pointer || touch.x < 0 || touch.x > Config.viewWidth || touch.y < 0 || touch.y > Config.viewHeight) {
			return true;
		} else {
			touchpad.addAction(Actions.alpha(0f, 0.2f, Interpolation.pow2In));
			return false;
		}
	}

}