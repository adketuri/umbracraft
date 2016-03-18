package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.definitions.hero.HeroDefinition;

/** A collection of current character stats
 * @author Andrew Keturi */
public class MemberStats {
	public int hp, maxHp, level, ep, maxEp, exp, maxExp;

	public MemberStats(HeroDefinition hero) {
		hp = hero.hp;
		maxHp = hero.hp;
		ep = 10;
	}

}
