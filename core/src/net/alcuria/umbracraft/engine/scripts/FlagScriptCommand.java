package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.FlagDefinition;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.FlagChangedEvent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Changes the value of a flag.
 * @author Andrew Keturi */
public class FlagScriptCommand extends ScriptCommand {

	public boolean enable;
	public String id;

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Flag: %s, %b", id, enable);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", new Array<String>() {
					{
						final ListDefinition<FlagDefinition> flags = Editor.db().flags();
						for (String key : flags.keys()) {
							add(flags.get(key).getName());
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
		Game.flags().set(id, enable);
		Game.publisher().publish(new FlagChangedEvent(id, entity));
	}

	@Override
	public void update() {
		complete();
	}

}
