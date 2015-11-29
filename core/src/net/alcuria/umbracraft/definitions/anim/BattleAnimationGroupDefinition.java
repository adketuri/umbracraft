package net.alcuria.umbracraft.definitions.anim;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines battle animations.
 * @author Andrew Keturi */
public class BattleAnimationGroupDefinition extends Definition {
	/** The attack animation */
	public String attack;
	/** The moving away animation */
	public String away;
	/** The dead animation */
	public String dead;
	/** The hurt animation */
	public String hurt;
	/** The idle animation */
	public String idle;
	/** The name */
	public String name;
	/** The moving towards animation */
	public String towards;

	@Override
	public String getName() {
		return name != null ? name : "Untitled";
	}

}