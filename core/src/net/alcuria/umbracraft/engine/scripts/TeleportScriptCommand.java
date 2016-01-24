package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to teleport a user to another map
 * @author Andrew Keturi */
public class TeleportScriptCommand extends ScriptCommand {

	private final float FADE_TIME = 0.5f;
	public String id = "";
	private boolean teleported;
	private float time;

	public TeleportScriptCommand(final String id) {
		this.id = id;
	}

	@Override
	public Set<String> getFilter() {
		// TODO Auto-generated method stub
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
		teleported = false;
		time = 0;
	}

	@Override
	public void onStarted(Entity entity) {
		time = 0;
		teleported = false;
	}

	@Override
	public void update() {
		if (time < FADE_TIME) {
			// fade out
			final float color = (1 - time / FADE_TIME) * (1 - time / FADE_TIME);
			Game.batch().setColor(new Color(color, color, color, 1));
			time += Gdx.graphics.getDeltaTime();
		} else {
			// fade in
			final float color = ((time - FADE_TIME) / FADE_TIME) * ((time - FADE_TIME) / FADE_TIME);
			Game.batch().setColor(new Color(color, color, color, 1));
			time += Gdx.graphics.getDeltaTime();
			if (!teleported) {
				teleported = true;
				time = FADE_TIME;
				Game.publisher().publish(new MapChangedEvent(id));
			}
			if (time >= 2 * FADE_TIME) {
				complete();
			}
		}
	}

}
