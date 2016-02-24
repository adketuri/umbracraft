package net.alcuria.umbracraft.definitions.npc;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.engine.components.ScriptComponent;

import com.badlogic.gdx.utils.Array;

/** Defines a a Script to be attached to a {@link ScriptComponent}. Scripts
 * consist of a list of states (or "Pages" in RPG Maker lingo) with their own
 * set of prerequisites and behaviors. The {@link ScriptComponent} is
 * responsible for determining which Pages to execute at run-time.
 * @author Andrew Keturi */
public class ScriptDefinition extends Definition {
	@Tooltip("The name of the script, for reference by the ScriptComponent.")
	public String name;
	@Tooltip("The pages, or states, of the script.")
	public Array<ScriptPageDefinition> pages;
	@Tooltip("A tag for sorting")
	public String tag;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
