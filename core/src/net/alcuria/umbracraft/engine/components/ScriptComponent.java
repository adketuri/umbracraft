package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition.ScriptTrigger;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.events.ScriptEndedEvent;
import net.alcuria.umbracraft.engine.events.ScriptStartedEvent;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand;
import net.alcuria.umbracraft.engine.scripts.PauseScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand.CommandState;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/** A component for handling scripted events, such as cutscenes. Consists of a
 * {@link ScriptPageDefinition} that is read and updated accordingly.
 * @author Andrew Keturi */
public class ScriptComponent implements Component, EventListener {

	private boolean active = false;
	private final Rectangle collisionRect = new Rectangle();
	private ScriptCommand currentCommand;
	private boolean pressed = false;
	private final String script;
	private ScriptPageDefinition scriptPage;
	private final Vector3 source = new Vector3();

	public ScriptComponent(String script) {
		this.script = script;
	}

	@Override
	public void create(final Entity entity) {
		//TODO: use script field to fetch the Script from the DB and create it here
		// create a dummy event page for now
		scriptPage = new ScriptPageDefinition();
		scriptPage.haltInput = true;
		scriptPage.trigger = ScriptTrigger.ON_INTERACTION;
		scriptPage.addCommand(new MessageScriptCommand("Start a battle"));
		scriptPage.addCommand(new PauseScriptCommand(0.4f));
		scriptPage.addCommand(new MessageScriptCommand("All done!"));
		//		scriptPage.commands = new Array<ScriptCommand>() {
		//			{
		//				add(Commands.message("Start a battle"));
		//				add(Commands.pause(0.4f));
		//				add(Commands.battle());
		//				add(Commands.message("You're done!"));
		//				//add(Commands.cameraTarget("Chest2"));
		//				//add(Commands.cameraTarget(Entity.PLAYER));
		//				//add(Commands.move("Chest", 2, 2, true));
		//				//add(Commands.teleport("Test", 5, 5));
		//				//add(Commands.showAnim(entity.getName(), "ChestAnim", true, false));
		//				//add(Commands.showAnim(Entity.PLAYER, "Spin", true, true));
		//				//add(Commands.pause(1));
		//				//add(Commands.message("This is not a mockup. I've finally implemented some simple messageboxes which should comfortably fit three or four lines. Still a work-in-progress, though."));
		//
		//			}
		//		};
		// listen for a key press
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
		Game.publisher().publish(new ScriptStartedEvent(scriptPage));
		// halt player movement
		Game.entities().find(Entity.PLAYER).velocity.set(0, 0, 0);
		currentCommand = scriptPage.command;
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
		// if we have some pages to execute, see if we can do that
		if (!active) {
			switch (scriptPage.trigger) {
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

	/** Updates the script, assumes all preconditions are met. (Eg., key has been
	 * pressed, etc.) */
	private void updateScript() {
		// if its done, increment our index
		switch (currentCommand.getState()) {
		case COMPLETE:
			currentCommand.setState(CommandState.NOT_STARTED);
			currentCommand = currentCommand.getNext();
			break;
		case NOT_STARTED:
			currentCommand.start();
			break;
		case STARTED:
			currentCommand.update();
			break;
		default:
			break;
		}
		// check if we're done with all scripts
		if (currentCommand == null) {
			active = false;
			Game.publisher().publish(new ScriptEndedEvent(scriptPage));
		}
	}
}
