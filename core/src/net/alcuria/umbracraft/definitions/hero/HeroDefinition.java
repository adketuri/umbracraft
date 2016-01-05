package net.alcuria.umbracraft.definitions.hero;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single hero.
 * @author Andrew Keturi */
public class HeroDefinition extends Definition {
	@Tooltip("The hero's HP. More is better.")
	public int hp;
	@Tooltip("The max level this hero may reach.")
	public int maxLevel;
	@Tooltip("The hero's displayed name.")
	public String name;

	@Override
	public String getName() {
		return name != null ? name : "Hero";
	}
}
