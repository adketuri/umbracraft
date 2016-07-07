package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/** Manages all of the game's tiles needed to render a map, both standard and
 * auto-tiled.
 * @author Andrew Keturi */
public class TileView {

	private final Array<TextureRegion> tiles;

	public TileView(String filename) {
		tiles = new Array<TextureRegion>();
		tiles.addAll(getRegions(filename));
	}

	public TextureRegion get(int id) {
		return tiles.get(id);
	}

	//FIXME: this REALLY shouldn't hardcode the tile location
	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		if (filename == null) {
			throw new NullPointerException("Tileset filename is null");
		}
		Array<TextureRegion> regions = new Array<TextureRegion>();
		final Texture texture = Game.assets().get("tiles/" + filename + ".png", Texture.class);
		for (int i = 0; i < Math.pow(Config.tilesetWidth / Config.tileWidth, 2); i++) {
			final int x = (i * Config.tileWidth) % Config.tilesetWidth;
			final int y = (i / Config.tileWidth) * Config.tileWidth;
			final int w = Config.tileWidth;
			regions.add(new TextureRegion(texture, x, y, w, w));
		}
		//		}
		return regions;
	}

}
