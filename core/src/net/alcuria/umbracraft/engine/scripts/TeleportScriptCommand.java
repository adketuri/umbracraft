package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.events.TintScreenEvent;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to teleport a user to another map
 * @author Andrew Keturi */
public class TeleportScriptCommand extends ScriptCommand {

	private final float FADE_TIME = 0.5f;
	public String id = "";

	public TeleportScriptCommand(final String id) {
		this.id = id;
	}

	@Override
	public ScriptCommand copy() {
		return new TeleportScriptCommand(id);
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "teleport";
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
		Game.publisher().publish(new TintScreenEvent(1, FADE_TIME, new Listener() {

			@Override
			public void invoke() {
				Game.publisher().publish(new MapChangedEvent(id));
				Game.publisher().publish(new TintScreenEvent(0, FADE_TIME, new Listener() {

					@Override
					public void invoke() {
						complete();
					}
				}));
			}
		}));
	}

	@Override
	public void update() {
	}

}
