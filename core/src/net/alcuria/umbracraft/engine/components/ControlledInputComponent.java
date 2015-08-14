package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/** A component specifically for handling object input from the player.
 * @author Andrew Keturi */
public class ControlledInputComponent implements Component, InputProcessor, EventListener {
	private static final int MARGIN = 4;
	private AnimationCollectionComponent group;
	private boolean haltInput;
	private int inputAltitude = 0;
	private final Vector3 inspectPos = new Vector3();
	private int keycode = 0;
	private Vector3 lastTouch;
	private MapCollisionComponent physics;

	@Override
	public void create(Entity entity) {
		lastTouch = new Vector3();
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose(Entity entity) {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER) {
			this.keycode = keycode;
			return true;
		} else if (keycode == Keys.F1) {
			Game.setDebug(!Game.isDebug());
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
	public void onEvent(Event event) {
		// check from any started scripts if we should pause input
		// TODO: probably use a counter here so concurrent events don't get messy
		if (event instanceof ScriptStartedEvent) {
			if (((ScriptStartedEvent) event).page.haltInput) {
				Game.log("Halting input");
				haltInput = true;
			}
		} else if (event instanceof ScriptEndedEvent) {
			haltInput = false;
			keycode = 0;
			Game.log("Resuming input");
		}

	}

	@Override
	public void render(Entity entity) {
		if (Game.isDebug()) {
			Game.batch().draw(Game.assets().get("debug.png", Texture.class), inspectPos.x, inspectPos.y, 1, 1);
		}
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
		if (haltInput) {
			return;
		}
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
			physics = entity.getComponent(MapCollisionComponent.class);
			group = entity.getComponent(AnimationCollectionComponent.class);
			if (physics != null && group != null) {
				inspectPos.x = entity.position.x + physics.getWidth() / 2;
				inspectPos.y = entity.position.y + physics.getHeight() / 2;
				inspectPos.z = entity.position.z;
				final Direction d = group.getGroup().getDirection();
				if (d == Direction.UPRIGHT || d == Direction.RIGHT || d == Direction.DOWNRIGHT) {
					inspectPos.x += physics.getWidth() / 2 + MARGIN;
				}
				if (d == Direction.UPLEFT || d == Direction.LEFT || d == Direction.DOWNLEFT) {
					inspectPos.x -= physics.getWidth() / 2 + MARGIN;
				}
				if (d == Direction.UPLEFT || d == Direction.UP || d == Direction.UPRIGHT) {
					inspectPos.y += physics.getHeight() / 2 + MARGIN;
				}
				if (d == Direction.DOWNLEFT || d == Direction.DOWN || d == Direction.DOWNRIGHT) {
					inspectPos.y -= physics.getHeight() / 2 + MARGIN;
				}
				Game.publisher().publish(new KeyDownEvent(keycode, inspectPos));
				Game.log("KeyDown");
			}
			keycode = 0;
		}
	}
}
