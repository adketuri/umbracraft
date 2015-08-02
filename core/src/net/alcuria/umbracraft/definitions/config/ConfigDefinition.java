package net.alcuria.umbracraft.definitions.config;

import net.alcuria.umbracraft.definitions.Definition;

/** Contains various user-defined configurations for the game, such as starting
 * location, starting sprites, and so on.
 * @author Andrew Keturi */
public class ConfigDefinition extends Definition {
	/** The starting area */
	public String startingArea;
	/** The startin AreaNode */
	public String startingNode;

	@Override
	public String getName() {
		return "Configuration";
	}

}
