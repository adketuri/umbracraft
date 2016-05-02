package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.IndicatorComponent;
import net.alcuria.umbracraft.engine.components.IndicatorComponent.IndicatorType;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class IndicatorOperationCommand extends ScriptCommand {

	public String param = "";
	public boolean start = false;
	public String target = "";
	public IndicatorType type = IndicatorType.ITEM;

	@Override
	public ScriptCommand copy() {
		IndicatorOperationCommand cmd = new IndicatorOperationCommand();
		cmd.param = param;
		cmd.target = target;
		cmd.start = start;
		cmd.type = type;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Indicator: " + type + ", " + param + ", " + (start ? "start, " : "stop, ") + target;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("target", Editor.db().entities().keys());
				put("param", Editor.db().items().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		// get entity
		Entity targetEntity = Game.entities().find(target);
		if (targetEntity != null) {
			IndicatorComponent component = targetEntity.getComponent(IndicatorComponent.class);
			if (component != null) {
				if (start) {
					component.start(type, param);
				} else {
					component.stop(type);
				}
			} else {
				Game.error("Entity does not have IndicatorComponent: " + target);
			}
		} else {
			Game.error("Target not found: " + target);
		}
		complete();
	}

	@Override
	public void update() {

	}

}
