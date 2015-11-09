package net.alcuria.umbracraft.definitions.skill;

import net.alcuria.umbracraft.definitions.Definition;

public class SkillPositionDefinition extends Definition {

	public int x, y;

	public SkillPositionDefinition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String getName() {
		return "Position";
	}

}
