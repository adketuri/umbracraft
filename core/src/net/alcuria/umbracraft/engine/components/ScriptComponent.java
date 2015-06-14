package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.definitions.npc.ScriptPageDefinition;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.scripts.ScriptCommand;
import net.alcuria.umbracraft.engine.scripts.Scripts;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ScriptComponent implements BaseComponent {

	private int commandIndex = 0;
	private ScriptPageDefinition scriptPage;

	@Override
	public void create() {
		// create a dummy event page for now
		scriptPage = new ScriptPageDefinition();
		scriptPage.facing = Direction.DOWN;
		scriptPage.hidden = false;
		scriptPage.position = new Vector3(10, 10, 0);
		scriptPage.eventCommands = new Array<ScriptCommand>() {
			{
				add(Scripts.testEvent("hello 1"));
				add(Scripts.testEvent("hello 2"));
			}
		};
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(Entity entity) {

	}

	@Override
	public void update(Entity entity) {
		if (commandIndex < 0) {
			throw new IllegalStateException("Command index cannot be < 0");
		}

		if (commandIndex < scriptPage.eventCommands.size) {
			final ScriptCommand script = scriptPage.eventCommands.get(commandIndex);
			script.update();
			if (script.isDone()) {
				commandIndex++;
			}
		} else {

		}

	}
}
