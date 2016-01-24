package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

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

	}

	@Override
	public void onStarted(Entity entity) {
		Game.battle().start();
	}

	@Override
	public void update() {
		complete();
	}

}
