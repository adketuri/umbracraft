package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.engine.components.base.BaseComponent;
import net.alcuria.umbracraft.engine.components.base.GameObject;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/** A component specifically for handling object input.
 * @author Andrew Keturi */
public class InputComponent implements BaseComponent, InputProcessor {
	private Vector3 lastTouch;

	@Override
	public void create() {
		lastTouch = new Vector3();
	}

	@Override
	public void dispose() {

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
	public void render(GameObject object) {

	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		lastTouch.x = screenX;
		lastTouch.y = screenY;
		lastTouch.z = 0;
		App.camera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		lastTouch.x = screenX;
		lastTouch.y = screenY;
		lastTouch.z = 0;
		App.camera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public void update(GameObject object) {
		object.position.x = lastTouch.x;
		object.position.y = lastTouch.y;

	}

}
