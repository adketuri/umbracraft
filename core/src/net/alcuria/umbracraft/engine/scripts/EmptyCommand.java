package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** An empty command. For now this will do.
 * @author Andrew Keturi */
public class EmptyCommand extends ScriptCommand {

	@Override
	public ScriptCommand copy() {
		return new EmptyCommand();
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return null;
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		complete();
	}

	@Override
	public void update() {

	}

}
