package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.IgnorePopulate;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.TintScreenEvent;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A command to color the screen either instantaneous or over time;
 * @author Andrew Keturi */
public class TintScreenCommand extends ScriptCommand {

	@IgnorePopulate
	public String color;
	@Tooltip("The time taken, in seconds, to change the screen to this tint")
	public float duration;
	@Tooltip("Whether or not to halt script execution until this completes")
	public boolean wait;

	@Override
	public ScriptCommand copy() {
		TintScreenCommand cmd = new TintScreenCommand();
		cmd.color = color;
		cmd.duration = duration;
		cmd.wait = wait;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Tint Screen: %s, %f, %b", color, duration, wait);
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
		Listener listener = null;
		if (wait) {
			listener = new Listener() {

				@Override
				public void invoke() {
					complete();
				}
			};
		}
		Game.publisher().publish(new TintScreenEvent(Color.valueOf(color), duration, listener));
		if (!wait) {
			complete();
		}
	}

	@Override
	public void update() {

	}

}
