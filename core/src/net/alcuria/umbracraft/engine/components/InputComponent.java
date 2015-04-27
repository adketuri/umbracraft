package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.objects.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/** A component specifically for handling object input.
 * @author Andrew Keturi */
public class InputComponent implements BaseComponent, InputProcessor {
	private int inputAltitude = 0;
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
		switch (character) {
		case 'z':
			if (inputAltitude > 0) {
				inputAltitude -= 16;
			} else {
				inputAltitude = 0;
			}
			return true;
		case 'x':
			inputAltitude += 16;
			return true;
		}
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
		Game.camera().getCamera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		lastTouch.x = screenX;
		lastTouch.y = screenY;
		lastTouch.z = 0;
		Game.camera().getCamera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public void update(GameObject object) {
		object.velocity.x = 0;
		object.velocity.y = 0;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			object.velocity.y = 2;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			object.velocity.x = -2;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			object.velocity.y = -2;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			object.velocity.x = 2;
		}
		//		object.desiredPosition.x = lastTouch.x;
		//		object.desiredPosition.y = lastTouch.y;
		object.altitude = inputAltitude;
	}

}
