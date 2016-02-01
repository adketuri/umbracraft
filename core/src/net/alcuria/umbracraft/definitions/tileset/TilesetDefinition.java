package net.alcuria.umbracraft.definitions.tileset;

import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

/** Defines a single tileset. The values are indexes into the tileset.
 * @author Andrew Keturi */
public class TilesetDefinition extends Definition {
	@Tooltip("The top area where edges originate from. Usually ground with some darker cliffs visible around the perimeter.")
	public int edge;
	@Tooltip("The filename of the tileset with extension but no path. eg: forest.png")
	@Order(1)
	public String filename;
	@Tooltip("The base floor of the map ")
	public int floor;
	@Tooltip("The first obstacle")
	public int obstacle1;
	@Tooltip("The altitude of the first obstacle")
	public int obstacle1Altitude;
	@Tooltip("The height of the first obstacle")
	public int obstacle1Height;
	@Tooltip("The width of the first obstacle")
	public int obstacle1Width;
	@Tooltip("The tree/foliage overlay")
	public int overlay;
	@Tooltip("The id of the stairs")
	public int stairs;
	@Tooltip("The id of the first terrain")
	public int terrain1;
	@Tooltip("The id of the second terrain")
	public int terrain2;
	@Tooltip("The id of the third terrain")
	public int terrain3;
	@Tooltip("The id of the fourth terrain")
	public int terrain4;
	@Tooltip("The bottom of the first tree wall")
	public int treeWall;
	@Tooltip("The bottom center wall/cliffside id")
	public int wall;

	@Override
	public String getName() {
		return "Tileset";
	}
}
