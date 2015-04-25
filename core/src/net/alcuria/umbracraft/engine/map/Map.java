package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
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
	private final Tile[][] data;
	private TilesetDefinition def;
	private final int height;
	private final Array<TextureRegion> tiles;
	private final int width;

	public Map() {
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
		data = new Tile[width][height];
		altMap = new int[width][height];
		altMap[3][3] = 2;
		altMap[3][4] = 2;
		altMap[4][3] = 2;
		altMap[4][4] = 2;
		altMap[5][3] = 2;
		altMap[5][4] = 2;
		altMap[3][5] = 2;
		altMap[4][5] = 2;
		altMap[5][5] = 2;
		//---
		altMap[10][10] = 4;
		altMap[10][11] = 4;
		altMap[9][10] = 4;
		altMap[9][11] = 4;
		int curAlt = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				data[i][j] = new Tile(altMap[i][j], 0);
			}
		}

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (altMap[i][j] > curAlt) {
					int alt = altMap[i][j];
					data[i][j - alt].id = getEdgeData(i, j);
					while (alt > 0) {
						alt--;
						data[i][j - alt].id = getWallData(i, j, alt);
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/** For a given true coordinate, use the altMap to figure out which edge to
	 * place
	 * @param i
	 * @param j
	 * @return */
	private int getEdgeData(int i, int j) {
		if (def == null) {
			return 0;
		}
		// top right down left
		int mask = 0b0000;
		if (altMap[i][j - 1] < altMap[i][j]) {
			mask = mask ^ 0b1000;
		}
		if (altMap[i + 1][j] < altMap[i][j]) {
			mask = mask ^ 0b0100;
		}
		if (altMap[i][j + 1] < altMap[i][j]) {
			mask = mask ^ 0b0010;
		}
		if (altMap[i - 1][j] < altMap[i][j]) {
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

	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		final Texture texture = Game.assets().get("tiles/" + filename, Texture.class);
		Array<TextureRegion> regions = new Array<TextureRegion>();
		for (int i = 0; i < Math.pow(Game.config().tilesetWidth / Game.config().tileWidth, 2); i++) {
			final int x = (i * Game.config().tileWidth) % Game.config().tilesetWidth;
			final int y = (i / Game.config().tileWidth) * Game.config().tileWidth;
			final int w = Game.config().tileWidth;
			Game.log(x + " " + y);
			regions.add(new TextureRegion(texture, x, y, w, w));
		}
		return regions;
	}

	private int getWallData(int i, int j, int altOff) {
		int alt = altMap[i][j];
		if (altOff == 0) {
			if (data[i][j - alt].id == def.edgeBottomLeft) {
				return def.bottomLeftWall;
			} else if (data[i][j - alt].id == def.edgeBottom) {
				return def.bottomCenterWall;
			} else if (data[i][j - alt].id == def.edgeBottomRight) {
				return def.bottomRightWall;
			}
		} else {
			if (data[i][j - alt].id == def.edgeBottomLeft) {
				return def.middleLeftWall;
			} else if (data[i][j - alt].id == def.edgeBottom) {
				return def.middleCenterWall;
			} else if (data[i][j - alt].id == def.edgeBottomRight) {
				return def.middleRightWall;
			}
		}
		return 0;
	}

	/** Renders every visible layer. */
	public void render() {
		final int tileSize = Game.config().tileWidth;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j] != null) {
					Game.batch().draw(tiles.get(data[i][j].id), (i * tileSize), (j * -tileSize), tileSize, tileSize);
				}
			}
		}
	}

	public void update(float delta) {

	}

}
