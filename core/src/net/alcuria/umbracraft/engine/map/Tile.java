package net.alcuria.umbracraft.engine.map;

/** A Tile represents a single unit of space on the {@link Map}
 * @author Andrew Keturi */
public class Tile {
	final int altitude;
	int id;
	int overId;

	public Tile(int id, int altitude) {
		this.id = id;
		this.altitude = altitude;
	}
}