package net.alcuria.umbracraft.definitions.tileset;

import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single tileset. The values are indexes into the tileset.
 * @author Andrew Keturi */
public class TilesetDefinition extends Definition {
	/** The bottom wall from center */
	public int bottomCenterWall;
	/** The bottom left sides of the wall */
	public int bottomLeftWall;
	/** The bottom right sides of the wall */
	public int bottomRightWall;
	/** The southern edge */
	public int edgeBottom;
	/** The southwestern edge */
	public int edgeBottomLeft;
	/** The southeastern edge */
	public int edgeBottomRight;
	/** The western edge */
	public int edgeLeft;
	/** The eastern edge */
	public int edgeRight;
	/** The northern edge */
	public int edgeTop;
	/** The northwestern edge */
	public int edgeTopLeft;
	/** The northeastern edge */
	public int edgeTopRight;
	/** The filename of the tileset with extension but no path. eg: "forest.png" */
	public String filename;
	/** The base floor of the map */
	public int floor;
	/** The middle segment of the walls */
	public int middleCenterWall;
	/** The middle segment of the left side of the walls */
	public int middleLeftWall;
	/** The middle segment of the right side of the walls */
	public int middleRightWall;
	/** The id of the stairs */
	public int stairs;
	/** The id of the first terrain */
	public int terrain1;
	/** The id of the second terrain */
	public int terrain2;
	/** The id of the third terrain */
	public int terrain3;
	/** The id of the fourth terrain */
	public int terrain4;

	@Override
	public String getName() {
		return "Tileset";
	}
}
