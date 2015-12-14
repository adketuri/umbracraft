package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;

/** A {@link ScriptCommand} to initiate a battle.
 * @author Andrew Keturi */
public class BattleScriptCommand extends ScriptCommand {

	@Override
	public String getName() {
		return "Battle:";
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted() {
		Game.battle().start();
	}

	@Override
	public void update() {
		complete();
	}

}
