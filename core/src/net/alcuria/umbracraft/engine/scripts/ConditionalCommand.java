package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Allows control flow within scripts.
 * @author Andrew Keturi */
public class ConditionalCommand extends ScriptCommand {

	public ScriptCommand conditional = new MessageScriptCommand("inside cond");

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Conditional:";
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

	}

	@Override
	public void update() {

	}

}
