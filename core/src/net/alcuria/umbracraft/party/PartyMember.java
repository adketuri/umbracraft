package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.alcuria.umbracraft.definitions.skill.SkillDefinition;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;

/** A member of the {@link Party}.
 * @author Andrew Keturi */
public class PartyMember {

	private transient Entity battler;
	private String heroId;
	private final Array<SkillDefinition> skills = new Array<SkillDefinition>();
	private MemberStats stats;

	/** For deserialization */
	public PartyMember() {
	}

	/** @param heroId the id of hero from the {@link HeroDefinition} */
	public PartyMember(final String heroId) {
		stats = new MemberStats(heroId);
		this.heroId = heroId;
	}

	/** Gets the battler set from battle
	 * @return the {@link Entity} battler */
	public Entity getBattler() {
		if (battler == null) {
			throw new NullPointerException("PartyMember's battler is null");
		}
		return battler;
	}

	/** @return the {@link HeroDefinition} used to define this party member */
	public HeroDefinition getDefinition() {
		return Game.db().hero(heroId);
	}

	/** @return the {@link MemberStats} of this party member */
	public MemberStats getStats() {
		return stats;
	}

	/** Sets a reference to the entity used for battling. (Used elsewhere to
	 * reset stats after battle.)
	 * @param battler */
	public void setBattleEntity(Entity battler) {
		this.battler = battler;
	}

}
