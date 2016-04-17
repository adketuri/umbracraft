package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.party.PartyMember;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Command to recover the current party to full health
 * @author Andrew Keturi */
public class RecoverScriptCommand extends ScriptCommand {

	@Override
	public ScriptCommand copy() {
		return new RecoverScriptCommand();
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Heal Party";
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
		for (PartyMember member : Game.party().getMembers()) {
			member.getStats().hp = member.getStats().getMaxHp();
		}
		complete();
	}

	@Override
	public void update() {

	}

}
