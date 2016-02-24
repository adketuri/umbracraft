package net.alcuria.umbracraft.definitions.tileset;

import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.definitions.Definition;

import com.badlogic.gdx.utils.Array;

/** Defines the list of all tilesets in the game.
 * @author Andrew Keturi */
public class TilesetListDefinition extends Definition {
	@Tooltip("A tag for sorting")
	public String tag;

	public Array<TilesetDefinition> tiles;

	@Override
	public String getName() {
		return "Tileset List";
	}

	@Override
	public String getTag() {
		return tag != null ? tag : "";
	}
}
