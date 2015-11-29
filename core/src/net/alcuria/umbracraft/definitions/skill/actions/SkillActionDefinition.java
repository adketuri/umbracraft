package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines an action for the game's battle system. No logic is contained within
 * the definition.
 * @author Andrew Keturi */
public abstract class SkillActionDefinition extends Definition {

	@Override
	public String getName() {
		return "Action";
	}

}
