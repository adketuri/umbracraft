package net.alcuria.umbracraft.party;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.hero.HeroDefinition;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/** A collection of current character stats
 * @author Andrew Keturi */
public class MemberStats {
	private final Evaluator eval = new Evaluator();
	private final HeroDefinition hero;
	public int hp, maxHp, level, ep, maxEp, exp, expNeeded;

	public MemberStats(HeroDefinition hero) {
		this.hero = hero;
		hp = hero.hp;
		maxHp = hero.hp;
		level = hero.startingLevel;
		ep = 10;
		exp = 0;
		expNeeded = getExpNeeded(hero.expNeeded);
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
			expNeeded = getExpNeeded(hero.expNeeded);
			Game.log(hero.name + " reached level " + level + " | " + exp + "/" + expNeeded);
		}

	}

	private int getExpNeeded(String expression) {
		try {
			return (int) eval.getNumberResult(replaceVariables(expression));
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
