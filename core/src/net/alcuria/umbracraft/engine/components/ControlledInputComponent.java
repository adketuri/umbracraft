package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.events.TouchpadCreatedEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/** A component specifically for handling object input from the player.
 * @author Andrew Keturi */
public class ControlledInputComponent implements Component, EventListener {
	private static final int MARGIN = 4;
	private static final float MAX_SPEED = 2; // max speed of the entity
	private static final float MAX_SPEED_TIME = 0.12f; // time entity takes to reach max speed
	private static Touchpad touchpad;
	private AnimationCollectionComponent group;
	private int haltCounter = 0;
	private boolean haltInput;
	private float holdTimeX, holdTimeY;
	private final Vector3 inspectPos = new Vector3();
	private MapCollisionComponent physics;
	private boolean subscribed;

	@Override
	public void create(Entity entity) {
		if (subscribed) {
			return;
		}
		Game.log("ControlledInput: SUBSCRIBING");
		Game.publisher().subscribe(this);
		subscribed = true;
	}

	@Override
	public void dispose(Entity entity) {
		Game.log("ControlledInput: UNSUBSCRIBING");
		Game.publisher().unsubscribe(this);
		subscribed = false;
	}

	@Override
	public void onEvent(Event event) {
		// TODO: probably use a counter here so concurrent events don't get messy
		if (event instanceof ScriptStartedEvent) {
			if (((ScriptStartedEvent) event).page.haltInput) {
				haltCounter++;
				Game.log("Halting input. counter: " + haltCounter);
				haltInput = true;
			}
		} else if (event instanceof ScriptEndedEvent) {
			if (((ScriptEndedEvent) event).page.haltInput) {
				haltCounter--;
				if (haltCounter <= 0) {
					haltInput = false;
					Game.log("Resuming input");
					haltCounter = 0;
				}
			}
		} else if (event instanceof TouchpadCreatedEvent) {
			touchpad = ((TouchpadCreatedEvent) event).touchpad;
			Game.log("Set touchpad");
		}

	}

	@Override
	public void render(Entity entity) {
		if (Game.isDebug()) {
			Game.batch().draw(Game.assets().get("debug.png", Texture.class), inspectPos.x, inspectPos.y, 1, 1);
		}
	}

	@Override
	public void update(Entity entity) {
		if (haltInput) {
			return;
		}

		// check which keys are pressed and update velocity accordingly
		boolean pressingXKey = false;
		boolean pressingYKey = false;

		// check for touchpad input or keyboard input
		if (touchpad != null && touchpad.isTouched()) {
			entity.velocity.x = touchpad.getKnobPercentX() * MAX_SPEED;
			entity.velocity.y = touchpad.getKnobPercentY() * MAX_SPEED;
			pressingXKey = Math.abs(entity.velocity.x) > MAX_SPEED / 4;
			pressingYKey = Math.abs(entity.velocity.y) > MAX_SPEED / 4;
		} else {
			if (Gdx.input.isKeyPressed(Keys.W)) {
				entity.velocity.y = MAX_SPEED;
				if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D)) {
					entity.velocity.y *= 0.707f;
				}
				pressingYKey = true;
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				entity.velocity.x = -MAX_SPEED;
				if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
					entity.velocity.x *= 0.707f;
				}
				pressingXKey = true;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				entity.velocity.y = -MAX_SPEED;
				if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.D)) {
					entity.velocity.y *= 0.707f;
				}
				pressingYKey = true;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				entity.velocity.x = MAX_SPEED;
				if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
					entity.velocity.x *= 0.707f;
				}
				pressingXKey = true;
			}
			// apply any interpolation to the velocity
			holdTimeX += pressingXKey ? Gdx.graphics.getDeltaTime() : -Gdx.graphics.getDeltaTime();
			holdTimeX = MathUtils.clamp(holdTimeX, 0, MAX_SPEED_TIME);
			holdTimeY += pressingYKey ? Gdx.graphics.getDeltaTime() : -Gdx.graphics.getDeltaTime();
			holdTimeY = MathUtils.clamp(holdTimeY, 0, MAX_SPEED_TIME);
			entity.velocity.x *= holdTimeX / MAX_SPEED_TIME;
			entity.velocity.y *= holdTimeY / MAX_SPEED_TIME;
		}

		if (Gdx.input.isKeyJustPressed(Keys.F1)) {
			Game.setDebug(!Game.isDebug());
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
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
				Game.log("Pressed Enter, publishing KeyDownEvent");
				Game.publisher().publish(new KeyDownEvent(Keys.ENTER, inspectPos));
			}
		}
	}
}
