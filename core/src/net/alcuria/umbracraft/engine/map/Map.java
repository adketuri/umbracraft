package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;

/** An internal representation of a playable, explorable map. It consists largely
 * of an array of {@link Layer} objects and an array of {@link TextureRegion}
 * objects to render the map.
 * @author Andrew Keturi */
public class Map implements Disposable {
	private final int[][] altMap;
	private TilesetDefinition def;
	private final BitmapFont font;
	private final int height;
	private final Array<Layer> layers;
	private final Array<TextureRegion> tiles;
	private final int width;

	public Map() {
		font = Game.assets().get("fonts/message.fnt", BitmapFont.class);
		String filename = "";
		// json -> object
		Json json = new Json();
		final FileHandle handle = Gdx.files.external("umbracraft/tilesets.json");
		if (handle.exists()) {
			TilesetListDefinition definition = json.fromJson(TilesetListDefinition.class, handle);
			def = definition.tiles.first();
			filename = def.filename;
		}
		// create
		tiles = new Array<TextureRegion>();
		tiles.addAll(getRegions(filename));
		width = 20;
		height = 15;
		layers = new Array<Layer>();
		altMap = new int[width][height];

		// set dummy alts
		for (int i = 3; i < 9; i++) {
			for (int j = 3; j < 9; j++) {
				altMap[i][j] = 2;
			}
		}
		for (int i = 5; i < 7; i++) {
			for (int j = 5; j < 7; j++) {
				altMap[i][j] = 4;
			}
		}
		//---
		altMap[10][10] = 4;
		altMap[10][11] = 4;
		altMap[9][10] = 4;
		altMap[9][11] = 4;

		// create list of all heights
		Array<Integer> heights = new Array<Integer>();
		for (int i = 0; i < altMap.length; i++) {
			for (int j = 0; j < altMap[0].length; j++) {
				if (!heights.contains(Integer.valueOf(altMap[i][j]), false)) {
					heights.add(new Integer(altMap[i][j]));
				}
			}
		}
		heights.sort();

		for (Integer altitude : heights) {
			Layer l = new Layer();
			l.alt = altitude;
			l.data = new Tile[width][height];
			for (int i = 0; i < altMap.length; i++) {
				for (int j = 0; j < altMap[0].length; j++) {
					if (altMap[i][j] == altitude) {
						if (j + altitude >= 0 && j + altitude < altMap[0].length) {
							l.data[i][j + altitude] = new Tile(l.alt, l.alt);
							if (j - 1 >= 0 && j - 1 < altMap[0].length) { // if in range
								int drop = altMap[i][j] - altMap[i][j - 1];
								while (drop > 0) {
									l.data[i][(j + altitude) - drop] = new Tile(def.middleCenterWall, l.alt);
									drop--;
								}
							}
							//							for (int k = 1; k <= altitude; k++) {
							//								if (altMap[i][j - k] < altitude - altMap[i][j]) {
							//									l.data[i][(j + altitude) - k] = new Tile(def.middleCenterWall, l.alt);
							//								}
							//							}
						}
					}
				}
			}
			layers.add(l);
		}

		//		for (int i = 0; i < data.length; i++) {
		//			for (int j = 0; j < data[0].length; j++) {
		//				if (altMap[i][j] > curAlt) {
		//					int alt = altMap[i][j];
		//					data[i][j - alt].id = getEdgeData(i, j);
		//					while (alt > 0) {
		//						alt--;
		//						data[i][j - alt].id = getWallData(i, j, alt);
		//					}
		//				}
		//			}
		//		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		final Texture texture = Game.assets().get("tiles/" + filename, Texture.class);
		Array<TextureRegion> regions = new Array<TextureRegion>();
		for (int i = 0; i < Math.pow(Game.config().tilesetWidth / Game.config().tileWidth, 2); i++) {
			final int x = (i * Game.config().tileWidth) % Game.config().tilesetWidth;
			final int y = (i / Game.config().tileWidth) * Game.config().tileWidth;
			final int w = Game.config().tileWidth;
			regions.add(new TextureRegion(texture, x, y, w, w));
		}
		return regions;
	}

	/** Renders every visible layer. */
	public void render() {
		final int tileSize = Game.config().tileWidth;
		for (int k = 0; k < layers.size; k++) {
			final Tile[][] data = layers.get(k).data;
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					if (data[i][j] != null) {
						Game.batch().draw(tiles.get(data[i][j].id), (i * tileSize), (j * tileSize), tileSize, tileSize);
					}
				}
			}
		}
		for (int i = 0; i < altMap.length; i++) {
			for (int j = 0; j < altMap[0].length; j++) {
				font.draw(Game.batch(), String.valueOf(altMap[i][j]), i * tileSize + 6, j * tileSize + 14);
			}
		}
	}

	public void update(float delta) {

	}

}
