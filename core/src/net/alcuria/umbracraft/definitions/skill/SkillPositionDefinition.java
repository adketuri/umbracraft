package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.definitions.Definition;

/** Represents a skill target
 * @author Andrew Keturi */
public class SkillPositionDefinition extends Definition {

	public int x, y;

	/** For serialization */
	public SkillPositionDefinition() {

	}

	public SkillPositionDefinition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String getName() {
		return "Position";
	}

	@Override
	public String getTag() {
		return "";
	}

}
