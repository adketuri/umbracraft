package net.alcuria.umbracraft.definitions.skill.actions;

import net.alcuria.umbracraft.annotations.Tooltip;

/** Describes a screen shake skill action.
 * @author Andrew Keturi */
public class ShakeScreenSkillActionDefinition extends SkillActionDefinition {

	@Tooltip("The severity or impact of the shake. Higher shakes further.")
	public float amplitude;
	@Tooltip("The length the shake lasts, in seconds.")
	public float duration;
	@Tooltip("The rate at which the screen shakes. Higher is faster.")
	public float frequency;

	@Override
	public SkillActionDefinition cpy() {
		ShakeScreenSkillActionDefinition def = new ShakeScreenSkillActionDefinition();
		def.amplitude = amplitude;
		def.duration = duration;
		def.frequency = frequency;
		return def;
	}
}
