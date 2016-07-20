package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

/** Defines a timed hit, its duration and whether or not it may be defended
 * against.
 * @author Andrew Keturi */
public class TimedHitSkillActionDefinition extends SkillActionDefinition {
	/** The type of timing attack
	 * @author Andrew Keturi */
	public enum TimingType {
		ATTACK, BOTH, DEFEND
	}

	@Tooltip("Determines whether this timing is for attacking, blocking, or both")
	public TimingType type;

	@Tooltip("The length of time the player has (in secs) to get a timed hit")
	public float window;

	@Override
	public SkillActionDefinition cpy() {
		TimedHitSkillActionDefinition def = new TimedHitSkillActionDefinition();
		def.type = type;
		return def;
	}

}
