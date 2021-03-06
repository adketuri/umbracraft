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
	@Tooltip("Use legacy walls, fixes nasty bugs. This is a hack and will be deprecated.")
	public boolean legacyWalls;
	@Tooltip("The name of the tileset")
	public String name;
	public int obstacle1;
	@Tooltip("The tree/foliage overlay")
	public int overlay;
	@Tooltip("Misc pieces")
	public int overlayPiece1;
	@Tooltip("Misc pieces")
	public int overlayPiece2;
	@Tooltip("Misc pieces")
	public int overlayPiece3;
	@Tooltip("Misc pieces")
	public int overlayPiece4;
	@Tooltip("The id of the stairs")
	public int stairs;
	@Tooltip("A tag for sorting")
	public String tag;
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
	@Tooltip("The expected height of walls, in tiles. Taller walls will repeat the nth tile.")
	public int wallHeight;
	@Tooltip("The water tile")
	public int water;

	@Override
	public String getName() {
		return name != null ? name : "Unknown";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
