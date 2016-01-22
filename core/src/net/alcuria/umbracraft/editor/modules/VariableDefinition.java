package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a variable to be used in scripts.
 * @author Andrew Keturi */
public class VariableDefinition extends Definition {

	@Tooltip("A reminder for the purpose of the variable. Not shown ingame.")
	public String description;
	@Tooltip("A unique id")
	public String id;

	@Override
	public String getName() {
		return id;
	}

}
