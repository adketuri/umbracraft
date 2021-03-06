package net.alcuria.umbracraft.definitions;

/** Defines some components of the game's database, such as a tileset or an
 * animation. Classes should extend {@link Definition} and add the correct
 * parameters.
 * @author Andrew Keturi */
public abstract class Definition {
	public abstract String getName();

	public abstract String getTag();
}
