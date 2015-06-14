package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines an NPC.
 * @author Andrew Keturi */
public class NpcDefinition extends Definition {
	public String map;
	public String name;
	public Array<ScriptPageDefinition> pages;

	@Override
	public String getName() {
		return name;
	}
}
