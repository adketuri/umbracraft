package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.screens.SetInputEnabled;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to initiate a battle.
 * @author Andrew Keturi */
public class BattleScriptCommand extends ScriptCommand {

	public String id = "";

	public BattleScriptCommand() {

	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Battle:";
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", new Array<String>());
			}
		};
	}

	@Override
	public void onCompleted() {
		final Entity player = Game.entities().find(Entity.PLAYER);
		if (player != null) {
			player.velocity.x = 0;
			player.velocity.y = 0;
		}
		Game.publisher().publish(new SetInputEnabled(true));
	}

	@Override
	public void onStarted(Entity entity) {
		Game.publisher().publish(new SetInputEnabled(false));
		Game.battle().start();
	}

	@Override
	public void update() {
		complete();
	}
}
