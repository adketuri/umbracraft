package net.alcuria.umbracraft.definitions.tileset;

import net.alcuria.umbracraft.definitions.Definition;

public class TilesetDefinition extends Definition {
	/** The bottom wall from center */
	public int bottomCenterWall;
	/** The bottom left sides of the wall */
	public int bottomLeftWall;
	/** The bottom right sides of the wall */
	public int bottomRightWall;
	public int edgeBottom;
	public int edgeBottomLeft;
	public int edgeBottomRight;
	public int edgeLeft;
	public int edgeRight;
	public int edgeTop;
	public int edgeTopLeft;
	public int edgeTopRight;
	/** The filename of the tileset with extension but no path. eg: "forest.png" */
	public String filename;
	/** The base floor of the map */
	public int floor;
	public int middleCenterWall;
	public int middleLeftWall;
	public int middleRightWall;
}
