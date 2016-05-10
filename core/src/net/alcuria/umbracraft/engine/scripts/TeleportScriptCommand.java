package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.IgnorePopulate;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.events.TintScreenEvent;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to teleport a user to another area
 * @author Andrew Keturi */
public class TeleportScriptCommand extends ScriptCommand {

	@Tooltip("The name of the area we want to teleport to")
	public String area = "";
	@IgnorePopulate
	private final float FADE_TIME = 0.5f;
	@Tooltip("The node within the area we want to teleport to")
	public String node = "";
	@Tooltip("The x coordinate")
	public int x;
	@Tooltip("The y coordinate")
	public int y;

	public TeleportScriptCommand() {
	}

	public TeleportScriptCommand(final String area, final String node) {
		this.area = area;
		this.node = node;
	}

	@Override
	public ScriptCommand copy() {
		return new TeleportScriptCommand(area, node);
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Teleport: %s - %s (%d, %d)", area, node, x, y);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("area", Editor.db().areas().keys());
				if (Editor.db().area(area) != null) {
					put("node", Editor.db().area(area).getNodeNames());
				}
			}
		};
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onStarted(Entity entity) {
		Game.publisher().publish(new TintScreenEvent(1, FADE_TIME, new Listener() {

			@Override
			public void invoke() {
				Game.publisher().publish(new MapChangedEvent(area, node, x, y));
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
