package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.ScriptComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A command to remove an entity from the map. The entity will return if the
 * player exits and re-enters the map.
 * @author Andrew Keturi */
public class RemoveEntityCommand extends ScriptCommand {

	public String id;
	public boolean self;

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Remove Entity: " + (self ? "(Self)" : id);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", Editor.db().entities().keys());
			}
		};
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onStarted(Entity entity) {
		final ScriptComponent script = self ? entity.getComponent(ScriptComponent.class) : Game.entities().find(id).getComponent(ScriptComponent.class);
		if (script != null) {
			script.setInactive();
			Game.entities().getEntities().removeValue(self ? entity : Game.entities().find(id), true);
		}
		complete();
	}

	@Override
	public void update() {

	}

}
