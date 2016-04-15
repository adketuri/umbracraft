package net.alcuria.umbracraft.definitions.hero;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single hero.
 * @author Andrew Keturi */
public class HeroDefinition extends Definition {
	@Tooltip("The agility function")
	public String agiFunc;
	@Tooltip("A function for the exp needed to level")
	public String expNeededFunc;
	@Tooltip("The intelligence function")
	public String intFunc;
	@Tooltip("A function for the max HP of the hero")
	public String maxHpFunc;
	@Tooltip("The hero's displayed name.")
	@Order(1)
	public String name;
	@Tooltip("The level this character starts at")
	@Order(3)
	public int startingLevel;
	@Tooltip("The strength function")
	public String strFunc;
	@Tooltip("A tag for sorting")
	@Order(2)
	public String tag;
	@Tooltip("The vitality function")
	public String vitFunc;

	@Override
	public String getName() {
		return name != null ? name : "Hero";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
