package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;

import com.badlogic.gdx.utils.Array;

/** A member of the {@link Party}
 * @author Andrew Keturi */
public class PartyMember {
	public HeroDefinition hero;
	public Array<SkillDefinition> skills;
}
