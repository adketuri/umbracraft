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
	private final int maxAlt;
	private final Array<TextureRegion> tiles;
	private final int tileSide = 1;
	private final int tileTop = 2;
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

		//		set dummy alts
		for (int i = 3; i < 7; i++) {
			for (int j = 3; j < 7; j++) {
				altMap[i][j] = 1;
			}
		}
		for (int i = 4; i < 6; i++) {
			for (int j = 4; j < 6; j++) {
				altMap[i][j] = 3;
			}
		}
		//---
		altMap[10][8] = 4;
		altMap[10][9] = 4;
		altMap[9][8] = 4;
		altMap[9][9] = 4;
		altMap[10 + 4][8] = 2;
		altMap[10 + 4][9] = 2;
		altMap[9 + 4][8] = 2;
		altMap[9 + 4][9] = 2;
		altMap[10 + 8][8] = 1;
		altMap[10 + 8][9] = 1;
		altMap[9 + 8][8] = 1;
		altMap[9 + 8][9] = 1;
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

		// build the layer
		int lastAlt = 0;
		for (Integer altitude : heights) {
			Layer l = new Layer();
			l.alt = altitude;
			l.data = new Tile[width][height];
			for (int i = 0; i < altMap.length; i++) {
				for (int j = 0; j < altMap[0].length; j++) {
					if (altMap[i][j] >= altitude) {
						if (j + altitude >= 0 && j + altitude < altMap[0].length) {
							l.data[i][j + altitude] = new Tile(createEdge(i, j, altitude), l.alt); // create edge
							// check if we need to create a wall
							if (j - 1 >= 0 && j - 1 < altMap[0].length) {
								int drop = (altitude - altMap[i][j - 1]);
								while (drop > 0) {
									l.data[i][(j + altitude) - drop] = new Tile(createWall(i, j, drop, altitude, altMap[i][j - 1]), l.alt);
									drop--;
								}
							}
						}
					}
				}
			}
			layers.add(l);
			lastAlt = l.alt;
		}

	}

	private boolean altContains(int i, int j) {
		return i >= 0 && i < altMap.length && j >= 0 && j < altMap[0].length;
	}

	private int createEdge(int i, int j, int altitude) {
		if (def == null) {
			return 0;
		}
		// top right down left
		int mask = 0b0000;
		if (altContains(i, j + 1) && altMap[i][j + 1] < altitude) {
			mask = mask ^ 0b1000;
		}
		if (altContains(i + 1, j) && altMap[i + 1][j] < altitude) {
			mask = mask ^ 0b0100;
		}
		if (altContains(i, j - 1) && altMap[i][j - 1] < altitude) {
			mask = mask ^ 0b0010;
		}
		if (altContains(i - 1, j) && altMap[i - 1][j] < altitude) {
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
		return 0;
	}

	private int createWall(int i, int j, int drop, int altitude, int baseAlt) {
		if (drop == altitude - baseAlt) {
			// lower walls
			if (altContains(i - 1, j) && altMap[i - 1][j] < altitude) {
				return def.bottomLeftWall;
			} else if (altContains(i + 1, j) && altMap[i + 1][j] < altitude) {
				return def.bottomRightWall;
			} else {
				return def.bottomCenterWall;
			}
		} else {
			// upper walls
			if (altContains(i - 1, j) && altMap[i - 1][j] < altitude) {
				return def.middleLeftWall;
			} else if (altContains(i + 1, j) && altMap[i + 1][j] < altitude) {
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

	/** @return the tallest altitude on this map */
	public int getMaxAltitude() {
		return maxAlt;
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

	/** Renders every visible layer.
	 * @param row */
	public void render(int row) {
		final int tileSize = Game.config().tileWidth;
		for (int k = 0; k < layers.size; k++) {
			int alt = layers.get(k).alt;
			final Tile[][] data = layers.get(k).data;
			//row += alt;
			if (row < 0 || row >= altMap[0].length) {
				return;
			}
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j <= altMap[i][row]; j++) {
					if (row >= 0 && row < data[i].length && data[i][row + j] != null) {
						Game.batch().draw(tiles.get(data[i][row + j].id), (i * tileSize), (row * tileSize) + j * 16, tileSize, tileSize);
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
