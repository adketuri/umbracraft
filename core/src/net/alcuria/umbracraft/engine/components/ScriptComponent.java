package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition.StartCondition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.BaseEvent;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.scripts.Commands;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/** A component for handling scripted events, such as cutscenes. Consists of a
 * {@link ScriptPageDefinition} that is read and updated accordingly.
 * @author Andrew Keturi */
public class ScriptComponent implements BaseComponent, EventListener {

	private boolean active = false;
	private int commandIndex = 0;
	private boolean pressed = false;
	private ScriptPageDefinition scriptPage;
	private Vector3 source;

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
				add(Commands.showAnim(entity.getName(), "ChestAnim", true, false));
				add(Commands.showAnim(Entity.PLAYER, "Spin", true, true));
				add(Commands.pause(1));

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
	public void onEvent(BaseEvent event) {
		if (event instanceof KeyDownEvent) {
			pressed = true;
			source = ((KeyDownEvent) event).source;
		}
	}

	@Override
	public void render(Entity entity) {

	}

	/** Checks if two entities are in close proximity, using the source vector
	 * @param entity the {@link Entity}
	 * @return true if the vector is close */
	private boolean touching(Entity entity) {
		return source != null && source.epsilonEquals(entity.position, 15);
	}

	@Override
	public void update(Entity entity) {
		if (commandIndex < 0) {
			throw new IllegalStateException("Command index cannot be < 0");
		}
		// if we have some pages to execute, see if we can do that
		if (commandIndex < scriptPage.commands.size) {
			switch (scriptPage.start) {
			case INSTANT:
				updateScript();
				break;
			case ON_INTERACTION:
				if (pressed && touching(entity)) {
					updateScript();
					scriptPage.start = StartCondition.INSTANT;
				}
			default:

			}
		} else {

		}

	}

	/** Updates the script, assumes all preconditions are met. (Eg., key has been
	 * pressed, etc.) */
	private void updateScript() {
		// first time starting, publish an event
		final ScriptCommand script = scriptPage.commands.get(commandIndex);
		if (!active) {
			Game.publisher().publish(new ScriptStartedEvent(scriptPage));
			active = true;
		}
		// start the script if necessary
		if (!script.hasStarted() && !script.isDone()) {
			script.start();
		}
		// update our script
		script.update();
		// if its done, increment our index
		if (script.isDone()) {
			commandIndex++;
			Game.log("Incrementing " + commandIndex);
		}
		// check if we're done
		if (active && commandIndex >= scriptPage.commands.size) {
			Game.publisher().publish(new ScriptEndedEvent(scriptPage));
			active = false;
		}
	}
}
