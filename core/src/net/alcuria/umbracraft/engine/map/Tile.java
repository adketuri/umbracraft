package net.alcuria.umbracraft.engine.map;

public class Tile {
	final int id;
	final boolean passable;

	public Tile(int id, boolean passable) {
		this.id = id;
		this.passable = passable;
	}
}