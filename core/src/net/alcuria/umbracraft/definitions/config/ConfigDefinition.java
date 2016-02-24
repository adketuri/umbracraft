package net.alcuria.umbracraft.definitions.config;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Contains various user-defined configurations for the game, such as starting
 * location, starting sprites, and so on.
 * @author Andrew Keturi */
public class ConfigDefinition extends Definition {
	@Tooltip("The global entities")
	public Array<String> globalEntities = new Array<String>();
	@Tooltip("The name of the starting area")
	public String startingArea;
	@Tooltip("The name of the node to start on within the area")
	public String startingNode;
	@Tooltip("The player's starting x coordinate, in tiles")
	public int startingX;
	@Tooltip("The player's starting y coordinate, in tiles")
	public int startingY;

	@Override
	public String getName() {
		return "Configuration";
	}

	@Override
	public String getTag() {
		return "";
	}

}
