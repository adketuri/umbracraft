package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to target the camera to a particular entity.
 * @author Andrew Keturi */
public class CameraTargetScriptCommand extends ScriptCommand {

	public String name = "";
	public float x, y;

	public CameraTargetScriptCommand() {
	}

	@Override
	public CameraTargetScriptCommand copy() {
		CameraTargetScriptCommand command = new CameraTargetScriptCommand();
		command.name = name;
		command.x = x;
		command.y = y;
		return command;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Camera Operations: %s, %f, %f", name, x, y);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("name", new Array<String>() {
					{
						final ListDefinition<EntityDefinition> entities = Editor.db().entities();
						for (String key : entities.keys()) {
							add(entities.get(key).getName());
						}
					}
				});
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		Entity target = Game.entities().find(name);
		if (target != null) {
			Game.view().pan(0, 0);
			Game.publisher().publish(new CameraTargetEvent(target));
		} else {
			Game.view().pan(x, y);
			Game.log("Entity not found: " + name + ". Cannot target.");
		}
		complete();
	}

	@Override
	public void update() {

	}

}
