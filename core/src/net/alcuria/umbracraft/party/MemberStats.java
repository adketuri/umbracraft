package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.util.O;
import net.sourceforge.jeval.EvaluationException;

import com.badlogic.gdx.utils.ObjectMap;

/** A collection of current character stats. Most of the stats are obtained via
 * getters that parse the stats from the definition
 * @author Andrew Keturi */
public class MemberStats {
	public static enum BaseStat {
		AGILITY, STRENGTH, VITALITY, WISDOM;

		public String getFunc(String hero) {
			O.notNull(hero);
			switch (this) {
			case STRENGTH:
				return Game.db().hero(hero).strFunc;
			case AGILITY:
				return Game.db().hero(hero).agiFunc;
			case VITALITY:
				return Game.db().hero(hero).vitFunc;
			case WISDOM:
				return Game.db().hero(hero).wisFunc;
			default:
				return "0";
			}
		}
	}

	private ObjectMap<BaseStat, Integer> baseStats;
	private int expNeeded;
	private String hero;
	public int hp, level, ep, maxEp, exp;
	private int maxHp;

	/** For deserialization */
	public MemberStats() {
	}

	public MemberStats(String hero) {
		this.hero = hero;
		level = Game.db().hero(hero).startingLevel;
		ep = 10;
		exp = 0;
		baseStats = new ObjectMap<MemberStats.BaseStat, Integer>();
		expNeeded = parse(Game.db().hero(hero).expNeededFunc);
		maxHp = parse(Game.db().hero(hero).maxHpFunc);
		hp = maxHp;
	}

	/** Increments the member's level, updating exp and needed exp */
	public void awardExp(int expAwarded) {
		exp += expAwarded;
		while (exp >= expNeeded) {
			level++;
			exp -= expNeeded;
			if (exp < 0) {
				exp = 0;
			}
			expNeeded = parse(Game.db().hero(hero).expNeededFunc);
			Game.log(Game.db().hero(hero).name + " reached level " + level + " | " + exp + "/" + expNeeded);
		}

	}

	/** @return exp needed. The expression is parsed on-call to calculate, so it's
	 *         probably a good idea to cache the result if it needs to be called
	 *         often. */
	public int getExpNeeded() {
		expNeeded = parse(Game.db().hero(hero).expNeededFunc);
		return expNeeded;
	}

	/** @return the character's max hp. The expression is parsed on-call to
	 *         calculate, so it's probably a good idea to cache the result if it
	 *         needs to be called often. */
	public int getMaxHp() {
		maxHp = parse(Game.db().hero(hero).maxHpFunc);
		return maxHp;
	}

	/** @return The {@link PartyMember}'s strength. The expression is parsed
	 *         on-call to calculate, so it's probably a good idea to cache the
	 *         result if it needs to be called often. */
	public int getStat(BaseStat stat) {
		O.notNull(baseStats);
		int statVal = parse(stat.getFunc(hero));
		baseStats.put(stat, statVal);
		return statVal;
	}

	private int parse(String expression) {
		try {
			expression = replaceVariables(expression);
			return (int) Game.eval().getNumberResult(expression);
		} catch (EvaluationException | NullPointerException e) {
			Game.error("Could not parse expression: " + expression);
			return 0;
		}
	}

	private String replaceVariables(String expression) {
		if (!baseStats.containsKey(BaseStat.AGILITY)) {
			baseStats.clear();
			baseStats.put(BaseStat.STRENGTH, 0);
			baseStats.put(BaseStat.AGILITY, 0);
			baseStats.put(BaseStat.WISDOM, 0);
			baseStats.put(BaseStat.VITALITY, 0);
			for (BaseStat key : baseStats.keys()) {
				baseStats.put(key, parse(key.getFunc(hero)));
			}
		}
		int agility = baseStats.get(BaseStat.AGILITY);
		int vitality = baseStats.get(BaseStat.VITALITY);
		int wisdom = baseStats.get(BaseStat.WISDOM);
		int strength = baseStats.get(BaseStat.WISDOM);
		return expression.replace("lvl", String.valueOf(level)).replace("agi", String.valueOf(agility)).replace("vit", String.valueOf(vitality)).replace("wis", String.valueOf(wisdom)).replace("str", String.valueOf(strength));
	}
}
