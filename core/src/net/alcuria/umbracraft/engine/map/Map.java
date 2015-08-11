package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;

/** An internal representation of a playable, explorable map. It consists largely
 * of an array of {@link Layer} objects and an array of {@link TextureRegion}
 * objects to render the map.
 * @author Andrew Keturi */
public class Map implements Disposable {
	private int[][] altMap;
	private TilesetDefinition def;
	private final BitmapFont font = Game.assets().get("fonts/message.fnt", BitmapFont.class);
	private int height;
	private Array<Layer> layers;
	private int maxAlt;
	private Array<TextureRegion> tiles;
	private final int tileSide = 1;
	private final int tileTop = 2;
	private int width;

	public Map() {
		create();
	}

	private void create() {
		// json -> object
		String filename = "";
		Json json = new Json();
		final FileHandle handle = Gdx.files.external("umbracraft/tilesets.json");
		if (handle.exists()) {
			TilesetListDefinition definition = json.fromJson(TilesetListDefinition.class, handle);
			def = definition.tiles.first();
			filename = def.filename;
		}

		// create tiles from definition
		tiles = new Array<TextureRegion>();
		tiles.addAll(getRegions(filename));

		// create alt map from definition
		final MapDefinition mapDef = Game.db().map("Andrew");
		width = mapDef.getWidth();
		height = mapDef.getHeight();
		altMap = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				altMap[i][j] = mapDef.tiles.get(i).get(height - j - 1).altitude;
			}
		}

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
		maxAlt = heights.get(heights.size - 1);

		// build the layers
		layers = new Array<Layer>();
		for (Integer altitude : heights) {
			Layer layer = new Layer();
			layer.alt = altitude;
			layer.data = new Tile[width][height];
			for (int i = 0; i < altMap.length; i++) {
				for (int j = -getMaxAltitude(); j < altMap[0].length; j++) {
					if (getAltitudeAt(i, j) >= altitude) {
						if (isInBounds(i, j + altitude)) {
							layer.data[i][j + altitude] = new Tile(createEdge(i, j, altitude), layer.alt); // create edge
						}
						// check if we need to create a wall
						if (j - 1 >= 0 && j - 1 < altMap[0].length) {
							int drop = (altitude - getAltitudeAt(i, j - 1));
							while (drop > 0) {
								if (isInBounds(i, (j + altitude) - drop)) {
									layer.data[i][(j + altitude) - drop] = new Tile(createWall(i, j, drop, altitude, getAltitudeAt(i, j - 1)), layer.alt);
								}
								drop--;
							}
						}
					}
				}
			}
			layers.add(layer);
		}
	}

	private int createEdge(int i, int j, int altitude) {
		if (def == null) {
			return 0;
		}
		// top right down left
		int mask = 0b0000;
		if (getAltitudeAt(i, j + 1) < altitude) {
			mask = mask ^ 0b1000;
		}
		if (getAltitudeAt(i + 1, j) < altitude) {
			mask = mask ^ 0b0100;
		}
		if (getAltitudeAt(i, j - 1) < altitude) {
			mask = mask ^ 0b0010;
		}
		if (getAltitudeAt(i - 1, j) < altitude) {
			mask = mask ^ 0b0001;
		}
		// now to switch on every possibility
		switch (mask) {
		case 0b0001:
			return def.edgeLeft;
		case 0b0010:
			return def.edgeBottom;
		case 0b0100:
			return def.edgeRight;
		case 0b1000:
			return def.edgeTop;
		case 0b1100:
			return def.edgeTopRight;
		case 0b1001:
			return def.edgeTopLeft;
		case 0b0110:
			return def.edgeBottomRight;
		case 0b0011:
			return def.edgeBottomLeft;
		}
		//TODO: More cases (0101, 1010, 1111, etc)
		return 0;
	}

	private int createWall(int i, int j, int drop, int altitude, int baseAlt) {
		if (drop == altitude - baseAlt) {
			// lower walls
			if (getAltitudeAt(i - 1, j) < altitude) {
				return def.bottomLeftWall;
			} else if (getAltitudeAt(i + 1, j) < altitude) {
				return def.bottomRightWall;
			} else {
				return def.bottomCenterWall;
			}
		} else {
			// upper walls
			if (getAltitudeAt(i - 1, j) < altitude) {
				return def.middleLeftWall;
			} else if (getAltitudeAt(i + 1, j) < altitude) {
				return def.middleRightWall;
			} else {
				return def.middleCenterWall;
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/** @param f
	 * @param g
	 * @return the altitude at tile f, g */
	public float getAltitudeAt(float f, float g) {
		return getAltitudeAt((int) f, (int) g);
	}

	/** Gets the altitude at some tile coordinates and does bounds checking too!
	 * @param x x tile
	 * @param y y tile
	 * @return */
	public int getAltitudeAt(int x, int y) {
		// clamp to the size of the map so it's assumed tiles outside the map are the same as edge tiles
		x = MathUtils.clamp(x, 0, altMap.length - 1);
		y = MathUtils.clamp(y, 0, altMap[0].length - 1);
		return altMap[x][y];
	}

	/** @return the height (not altitude) of the map */
	public int getHeight() {
		return height;
	}

	/** @return the tallest altitude on this map */
	public int getMaxAltitude() {
		return maxAlt;
	}

	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		final Texture texture = Game.assets().get("tiles/" + filename, Texture.class);
		Array<TextureRegion> regions = new Array<TextureRegion>();
		for (int i = 0; i < Math.pow(Config.tilesetWidth / Config.tileWidth, 2); i++) {
			final int x = (i * Config.tileWidth) % Config.tilesetWidth;
			final int y = (i / Config.tileWidth) * Config.tileWidth;
			final int w = Config.tileWidth;
			regions.add(new TextureRegion(texture, x, y, w, w));
		}
		return regions;
	}

	public int getWidth() {
		return width;
	}

	private boolean isInBounds(int i, int j) {
		return i >= 0 && i < altMap.length && j >= 0 && j < altMap[0].length;
	}

	/** Renders every visible layer.
	 * @param row the map row to render, in tiles
	 * @param xOffset the camera offset in tiles, to ensure we only render tiles
	 *        visible in the x axis */
	public void render(int row, int xOffset) {
		final int tileSize = Config.tileWidth;
		for (int k = 0; k < layers.size; k++) {
			int alt = layers.get(k).alt;
			final Tile[][] data = layers.get(k).data;
			if (row < 0 || row >= altMap[0].length) {
				return;
			}
			for (int i = xOffset, n = xOffset + Config.viewWidth / Config.tileWidth; i < n; i++) {
				// prevents bottom rows from creeping up during rendering
				// TODO: make this generic. i think for alts > 0 it will break
				int rowRenderHeight = alt == 0 ? 0 : getAltitudeAt(i, row);
				for (int j = 0; j <= rowRenderHeight; j++) {
					try {
						if (i >= 0 && i < data.length && row >= 0 && row < data[i].length && data[i][row + j] != null) {
							Game.batch().draw(tiles.get(data[i][row + j].id), (i * tileSize), (row * tileSize) + j * 16, tileSize, tileSize);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//FIXME: Halp. someting up with rendering very top and very bottom rows.
						// Game.log(i + " " + j + " " + row);
					}
				}
			}
		}
		//		for (int i = 0; i < altMap.length; i++) {
		//			for (int j = 0; j < altMap[0].length; j++) {
		//				font.draw(Game.batch(), String.valueOf(altMap[i][j]), i * tileSize + 6, j * tileSize + 14);
		//			}
		//		}
	}

	/** Updates the map
	 * @param delta the time since the last frame */
	public void update(float delta) {

	}
}
