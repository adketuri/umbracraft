package net.alcuria.umbracraft.definitions.hero;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single hero.
 * @author Andrew Keturi */
public class HeroDefinition extends Definition {
	@Tooltip("The hero's HP. More is better.")
	public int hp;
	@Tooltip("The hero's displayed name.")
	@Order(1)
	public String name;
	@Tooltip("A tag for sorting")
	public String tag;

	@Override
	public String getName() {
		return name != null ? name : "Hero";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
