package net.alcuria.umbracraft;

import net.alcuria.umbracraft.engine.scripts.BattleScriptCommand;

/** An interface for delegating any battle-specific code needed in UmbraCraft to
 * its implementing projects.
 * @author Andrew Keturi */
public interface Battle {

	/** Call to end the battle and return to the field screen. */
	void end();

	/** Call to start a battle.
	 * @param command */
	void start(BattleScriptCommand command);

}
