package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition.StartCondition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.scripts.Commands;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand.CommandState;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A component for handling scripted events, such as cutscenes. Consists of a
 * {@link ScriptPageDefinition} that is read and updated accordingly.
 * @author Andrew Keturi */
public class ScriptComponent implements Component, EventListener {

	private boolean active = false;
	private final Rectangle collisionRect = new Rectangle();
	private int commandIndex = 0;
	private boolean pressed = false;
	private ScriptPageDefinition scriptPage;
	private final Vector3 source = new Vector3();

	@Override
	public void create(final Entity entity) {
		// create a dummy event page for now
		scriptPage = new ScriptPageDefinition();
		scriptPage.haltInput = true;
		scriptPage.facing = Direction.DOWN;
		scriptPage.hidden = false;
		scriptPage.start = StartCondition.ON_INTERACTION;
		scriptPage.position = new Vector3(10, 10, 0);
		scriptPage.commands = new Array<ScriptCommand>() {
			{
				add(Commands.cameraTarget("Chest2"));
				add(Commands.message("That treasure chest looks tasty."));
				add(Commands.cameraTarget(Entity.PLAYER));
				add(Commands.move("Chest", 2, 2, true));
				//add(Commands.teleport("Test", 5, 5));
				//add(Commands.showAnim(entity.getName(), "ChestAnim", true, false));
				//add(Commands.showAnim(Entity.PLAYER, "Spin", true, true));
				//add(Commands.pause(1));
				//add(Commands.message("This is not a mockup. I've finally implemented some simple messageboxes which should comfortably fit three or four lines. Still a work-in-progress, though."));

			}
		};
		// listen for when a key is pressed
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose(Entity entity) {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof KeyDownEvent) {
			pressed = true;
			source.set(((KeyDownEvent) event).source);
		}
	}

	@Override
	public void render(Entity entity) {

	}

	/** Starts a script. should only be called once at the start */
	private void startScript() {
		// first time starting, publish an event
		Game.publisher().publish(new ScriptStartedEvent(scriptPage));
		active = true;
		pressed = false;
	}

	/** Checks if two entities are in close proximity, using the source vector.
	 * @param entity the {@link Entity}
	 * @return true if the vector is close */
	private boolean touching(Entity entity) {
		pressed = false;
		if (MathUtils.isEqual(source.z, entity.position.z)) {
			MapCollisionComponent collision = entity.getComponent(MapCollisionComponent.class);
			if (collision != null) {
				collisionRect.set(entity.position.x, entity.position.y, collision.getWidth(), collision.getHeight());
				return source != null && collisionRect.contains(source.x, source.y);
			}
		}
		return false;
	}

	@Override
	public void update(Entity entity) {
		if (commandIndex < 0) {
			throw new IllegalStateException("Command index cannot be < 0");
		}
		// if we have some pages to execute, see if we can do that
		if (commandIndex < scriptPage.commands.size) {
			if (!active) {
				switch (scriptPage.start) {
				case INSTANT:
					startScript();
					break;
				case ON_INTERACTION:
					if (pressed && touching(entity)) {
						startScript();
					}
				default:

				}
			} else {
				updateScript();
			}
		}

	}

	/** Updates the script, assumes all preconditions are met. (Eg., key has been
	 * pressed, etc.) */
	private void updateScript() {
		// update our script
		final ScriptCommand command = scriptPage.commands.get(commandIndex);
		// if its done, increment our index
		switch (command.getState()) {
		case COMPLETE:
			commandIndex++;
			break;
		case NOT_STARTED:
			command.start();
			break;
		case STARTED:
			command.update();
			break;
		default:
			break;
		}
		// check if we're done with all scripts
		if (commandIndex >= scriptPage.commands.size) {
			commandIndex = 0;
			for (ScriptCommand s : scriptPage.commands) {
				s.setState(CommandState.NOT_STARTED);
			}
			active = false;
			Game.publisher().publish(new ScriptEndedEvent(scriptPage));
		}
	}
}
