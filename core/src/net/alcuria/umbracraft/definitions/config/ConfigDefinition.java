package net.alcuria.umbracraft.definitions.config;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Contains various user-defined configurations for the game, such as starting
 * location, starting sprites, and so on.
 * @author Andrew Keturi */
public class ConfigDefinition extends Definition {
	@Tooltip("The path to battle faces on the turn order table. Relative")
	@Order(507)
	public String battleFacePath;
	@Tooltip("A function for the exp needed to level")
	@Order(700)
	public String expNeededFunc;
	@Tooltip("The path to faces. Relative")
	@Order(502)
	public String facePath;
	@Tooltip("The global entities")
	public Array<String> globalEntities = new Array<String>();
	@Tooltip("The path to icons. Relative")
	@Order(501)
	public String iconPath;
	@Tooltip("A function for magical damage calculations")
	@Order(702)
	public String magicalDamageFunc;
	@Tooltip("A function for the max HP of the heroes")
	@Order(701)
	public String maxHpFunc;
	@Tooltip("How much stuff the player may hold to begin with")
	public int maxWeight;
	@Order(505)
	@Tooltip("The path to particles. Relative")
	public String particlePath;
	@Tooltip("A function for physical damage calculations")
	@Order(704)
	public String physicalDamageFunc;
	@Tooltip("The path to the root of the game. Absolute.")
	@Order(500)
	public String projectPath;
	@Order(506)
	@Tooltip("The external path to save data")
	public String savePath;
	@Order(504)
	@Tooltip("The path to sounds. Relative")
	public String soundPath;
	@Order(503)
	@Tooltip("The path to sprites. Relative")
	public String spritePath;
	@Tooltip("The name of the starting area")
	public String startingArea;
	@Tooltip("The name of the node to start on within the area")
	public String startingNode;
	@Tooltip("The starting party")
	public Array<String> startingParty = new Array<String>();
	@Tooltip("The player's starting x coordinate, in tiles")
	public int startingX;
	@Tooltip("The player's starting y coordinate, in tiles")
	public int startingY;
	@Tooltip("The size of the charset templates")
	public int templateWidth, templateHeight;
	@Order(504)
	@Tooltip("The path to tilesets. Relative")
	public String tilesetPath;

	@Override
	public String getName() {
		return "Configuration";
	}

	@Override
	public String getTag() {
		return "";
	}

}
