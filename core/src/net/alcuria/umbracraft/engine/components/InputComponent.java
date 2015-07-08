package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/** A component specifically for handling object input.
 * @author Andrew Keturi */
public class InputComponent implements BaseComponent, InputProcessor {
	private int inputAltitude = 0;
	private int keycode = 0;
	private Vector3 lastTouch;

	@Override
	public void create(Entity entity) {
		lastTouch = new Vector3();
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			this.keycode = keycode;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		switch (character) {
		case 'z':
			if (inputAltitude > 0) {
				inputAltitude -= 1;
			} else {
				inputAltitude = 0;
			}
			return true;
		case 'x':
			inputAltitude += 1;
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
	public void render(Entity entity) {

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
		Game.view().getCamera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		lastTouch.x = screenX;
		lastTouch.y = screenY;
		lastTouch.z = 0;
		Game.view().getCamera().unproject(lastTouch);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public void update(Entity entity) {
		// update velocity
		entity.velocity.x = 0;
		entity.velocity.y = 0;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			entity.velocity.y = 2;
			if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D)) {
				entity.velocity.y *= 0.707f;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			entity.velocity.x = -2;
			if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
				entity.velocity.x *= 0.707f;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			entity.velocity.y = -2;
			if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D)) {
				entity.velocity.y *= 0.707f;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			entity.velocity.x = 2;
			if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
				entity.velocity.x *= 0.707f;
			}
		}
		// broadcast that a key was pressed
		if (keycode > 0) {
			Game.publisher().publish(new KeyDownEvent(keycode, entity.position));
			keycode = 0;
		}
	}

}
