package net.alcuria.umbracraft.engine.scripts;

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

	public CameraTargetScriptCommand() {
	}

	public CameraTargetScriptCommand(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return "Camera Target: " + name;
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
	public void onStarted() {
		Entity entity = Game.entities().find(name);
		if (entity != null) {
			Game.publisher().publish(new CameraTargetEvent(entity));
		} else {
			Game.log("Entity not found: " + name + ". Cannot target.");
		}
		complete();
	}

	@Override
	public void update() {

	}

}
