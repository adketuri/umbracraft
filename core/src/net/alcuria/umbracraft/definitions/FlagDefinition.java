package net.alcuria.umbracraft.definitions;

import net.alcuria.umbracraft.Game;

/** Defines a flag (or "switch" in RPG Maker 2003 terms) that is toggled during
 * scripted events. This definition is stateless. All run-time changes to flags
 * are maintained in the {@link Game} class.
 * @author Andrew Keturi */
public class FlagDefinition extends Definition {

	public String description;
	public String id;

	@Override
	public String getName() {
		return id;
	}

}
