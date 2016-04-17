package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ItemChangeScriptCommand extends ScriptCommand {

	@Order(2)
	@Tooltip("The amount to add/subtract. Negative values will remove.")
	public int amount = 0;
	@Order(1)
	@Tooltip("The item ID to change")
	public String id = "";

	@Override
	public ScriptCommand copy() {
		ItemChangeScriptCommand item = new ItemChangeScriptCommand();
		item.amount = amount;
		item.id = id;
		return item;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Change Inventory: " + id + ", " + (amount >= 0 ? "Add " : "Remove ") + Math.abs(amount);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", Editor.db().items().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		if (amount > 0) {
			Game.items().add(id, amount);
		} else if (amount < 0) {
			Game.items().remove(id, -amount);
		}
		complete();
	}

	@Override
	public void update() {

	}
}
