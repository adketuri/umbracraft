package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.sourceforge.jeval.EvaluationException;

/** A collection of current character stats
 * @author Andrew Keturi */
public class MemberStats {
	private int expNeeded;
	private HeroDefinition hero;
	public int hp, level, ep, maxEp, exp;
	private int maxHp;

	/** For deserialization */
	public MemberStats() {
	}

	public MemberStats(HeroDefinition hero) {
		this.hero = hero;
		level = hero.startingLevel;
		ep = 10;
		exp = 0;
		expNeeded = parse(hero.expNeededFunc);
		maxHp = parse(hero.maxHpFunc);
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
			expNeeded = parse(hero.expNeededFunc);
			Game.log(hero.name + " reached level " + level + " | " + exp + "/" + expNeeded);
		}

	}

	public int getExpNeeded() {
		expNeeded = parse(hero.expNeededFunc);
		return expNeeded;
	}

	public int getMaxHp() {
		maxHp = parse(hero.maxHpFunc);
		return maxHp;
	}

	private int parse(String expression) {
		try {
			return (int) Game.eval().getNumberResult(replaceVariables(expression));
		} catch (EvaluationException e) {
			System.err.println("Could not parse expression: " + expression);
			e.printStackTrace();
			return 0;
		}
	}

	private String replaceVariables(String expression) {
		return expression.replace("lvl", String.valueOf(level));
	}
}
