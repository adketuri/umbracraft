package net.alcuria.umbracraft.definitions.hero;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single hero.
 * @author Andrew Keturi */
public class HeroDefinition extends Definition {
	/** The hero's HP */
	public int hp;
	/** The hero's max level */
	public int maxLevel;
	/** the hero's name */
	public String name;

	@Override
	public String getName() {
		return "Hero";
	}
}
