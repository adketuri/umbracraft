package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;

import com.badlogic.gdx.utils.Array;

/** A member of the {@link Party}
 * @author Andrew Keturi */
public class PartyMember {
	private int currentHp;
	private final HeroDefinition hero;
	private final Array<SkillDefinition> skills = new Array<SkillDefinition>();

	public PartyMember(final String heroId) {
		hero = Game.db().hero(heroId);
		currentHp = hero.hp;
	}

	public int getCurrentHp() {
		return currentHp;
	}

	public HeroDefinition getDefinition() {
		return hero;
	}

	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

}
