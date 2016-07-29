package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EntityVisibilityScriptCommand extends ScriptCommand {
	@Tooltip("The entity id we wish to change visibility of")
	public String id = "";
	@Tooltip("If checked, applies to self")
	public boolean self;
	@Tooltip("The visibility")
	public boolean visible;

	@Override
	public ScriptCommand copy() {
		EntityVisibilityScriptCommand cmd = new EntityVisibilityScriptCommand();
		cmd.id = id;
		cmd.self = self;
		cmd.visible = visible;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Set Visibility: %s, %b", self ? "(Self)" : id, visible);
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
		Entity target = self ? entity : Game.entities().find(id);
		if (target != null) {
			entity.setVisible(visible);
		} else {
			Game.error("Cannot change visibility. No entity found with id " + id);
		}
		complete();
	}

	@Override
	public void update() {

	}
}
