package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.util.O;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Manages all of the game's tile texture regions needed to render a map, both
 * standard and auto-tiled. Only one instance of this class is needed per map
 * object.
 * @author Andrew Keturi */
public class TileView {

	private static final int[] xOffsets = { 0, 1, 0, 1 };
	private static final int[] yOffsets = { 1, 1, 0, 0, };
	private final ObjectMap<Integer, Array<TextureRegion>> autoTiles;
	private final Array<TextureRegion> tiles;

	public TileView(String filename, TilesetDefinition definition) {

		// ensure we have everything we need
		O.notNull(filename);
		O.notNull(definition);
		// add regular tile textures
		tiles = new Array<TextureRegion>();
		final Texture texture = Game.assets().get("tiles/" + filename + ".png", Texture.class); //FIXME: hardcoded, bad andrew !!
		for (int i = 0; i < Math.pow(Config.tilesetWidth / Config.tileWidth, 2); i++) {
			final int x = (i * Config.tileWidth) % Config.tilesetWidth;
			final int y = (i / Config.tileWidth) * Config.tileWidth;
			final int w = Config.tileWidth;
			tiles.add(new TextureRegion(texture, x, y, w, w));
		}

		// create autotile object map, mapping a texture id from the editor to an array of its regions
		autoTiles = new ObjectMap<Integer, Array<TextureRegion>>();
		final int[] terrainIds = { definition.terrain1, definition.terrain2, definition.terrain3, definition.terrain4, definition.overlay };

		// for each of the terrain types we have, if it's valid, add it to the map
		for (int j = 0; j < terrainIds.length; j++) {
			if (terrainIds[j] > 0) {

				// we have a valid terrain id, let's get it's absolute pixel coordinates on the tileset image
				Game.debug("Valid terrain: " + terrainIds[j]);
				final int absX = (terrainIds[j] * Config.tileWidth) % Config.tilesetWidth;
				final int absY = (terrainIds[j] / Config.tileWidth) * Config.tileWidth;
				Game.debug(String.format("got abs x=%d y=%d", absX, absY));

				// now, let's get a series zero-based local x and y coordinates for the autotiles
				Array<TextureRegion> regions = new Array<TextureRegion>();
				for (int i = 0; i < 48; i++) {
					final int locX = (i % 6) * Config.tileWidth / 2;
					final int locY = (i / 6) * Config.tileWidth / 2;

					// we can add each of these to the absolute pixel coordinates and get the location of the autotile region on the tileset
					Game.debug(String.format("Adding texture region at x=%d y=%d", absX + locX, absX + locY));
					regions.add(new TextureRegion(texture, absX + locX, absY + locY, Config.tileWidth / 2, Config.tileWidth / 2));
				}

				// lastly, add the newly-created array of autotile texture regions to our autotile object map
				autoTiles.put(terrainIds[j], regions);
			}
		}

	}

	/** Draws either the autotile or regular tile at this coordinate
	 * @param attributes tile attributes for this location
	 * @param x x coordinate
	 * @param y y coordinate */
	public void draw(AutoTileAttributes attributes, int x, int y) {
		final int type = attributes.getType();
		final int w = Config.tileWidth / 2;
		if (autoTiles.containsKey(type)) {
			Array<TextureRegion> autoTileRegions = autoTiles.get(type);

			// for each corner (top left, top right, bot left, bot right, we want to draw the autotile
			for (int idx = 0; idx < xOffsets.length; idx++) {
				int corner = attributes.getCorner(idx);
				Game.batch().draw(autoTileRegions.get(corner), x + xOffsets[idx] * w, y + yOffsets[idx] * w, w, w);
			}
		} else {
			Game.batch().draw(get(type), x, y, w * 2, w * 2);
		}
	}

	public TextureRegion get(int id) {
		return tiles.get(id);
	}

}
