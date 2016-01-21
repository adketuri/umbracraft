package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;

import com.badlogic.gdx.graphics.Texture;
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
	private int[][] altMap, typeMap;
	//	private final BitmapFont font = Game.assets().get("fonts/message.fnt", BitmapFont.class);
	private int height;
	private Array<Layer> layers;
	private int maxAlt;
	private String name;
	private Array<TextureRegion> tiles;
	private TilesetDefinition tilesetDefinition;
	private final int tileSide = 1;
	private final int tileTop = 2;
	private boolean[][] typeFlags;
	private int width;

	public void create(String id) {
		if (id == null) {
			throw new NullPointerException("id cannot be null. Perhaps an area node's mapDefinition field is null?");
		}
		name = id;
		// json -> object
		Json json = new Json();
		tilesetDefinition = Game.db().tileset(0);
		String filename = tilesetDefinition.filename;

		// create tiles from definition
		tiles = new Array<TextureRegion>();
		tiles.addAll(getRegions(filename));

		// create alt map from definition
		final MapDefinition mapDef = Game.db().map(id);
		if (mapDef == null) {
			throw new NullPointerException("Map not found: " + id);
		}
		width = mapDef.getWidth();
		height = mapDef.getHeight();
		altMap = new int[width][height];
		typeFlags = new boolean[width][height];
		typeMap = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				typeFlags[i][j] = false;
				altMap[i][j] = mapDef.tiles.get(i).get(height - j - 1).altitude;
				typeMap[i][j] = mapDef.tiles.get(i).get(height - j - 1).type;
				if (typeMap[i][j] == 1) {
					typeMap[i][j] = tilesetDefinition.terrain1;
				} else if (typeMap[i][j] == 2) {
					typeMap[i][j] = tilesetDefinition.terrain2;
				} else if (typeMap[i][j] == 3) {
					typeMap[i][j] = tilesetDefinition.terrain3;
				} else if (typeMap[i][j] == 4) {
					typeMap[i][j] = tilesetDefinition.terrain4;
				} else if (typeMap[i][j] == 5) {
					typeMap[i][j] = tilesetDefinition.stairs;
				}
			}
		}

		// remove unusable terrain
		//		for (int i = 0; i < width; i++) {
		//			for (int j = 0; j < height; j++) {
		//				if (getAltitudeAt(i - 2, j) < getAltitudeAt(i, j) || getAltitudeAt(i - 1, j) != getAltitudeAt(i, j)) {
		//					typeMap[i][j] = 0;
		//				}
		//				if (getAltitudeAt(i + 2, j) < getAltitudeAt(i, j) || getAltitudeAt(i + 1, j) != getAltitudeAt(i, j)) {
		//					typeMap[i][j] = 0;
		//				}
		//				if (getAltitudeAt(i, j - 2) < getAltitudeAt(i, j) || getAltitudeAt(i, j - 1) != getAltitudeAt(i, j)) {
		//					typeMap[i][j] = 0;
		//				}
		//				if (getAltitudeAt(i, j + 2) < getAltitudeAt(i, j) || getAltitudeAt(i, j + 1) != getAltitudeAt(i, j)) {
		//					typeMap[i][j] = 0;
		//				}
		//			}
		//		}

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				final int terrain = tilesetDefinition.terrain1;
				if (typeMap[i][j] == 0 && !typeFlags[i][j]) {
					// get surrounding mask
					// top topright right rightdown _ down downleft left lefttop
					int[] dX = { 0, 1, 1, 1, 0, -1, -1, -1 };
					int[] dY = { 1, 1, 0, -1, -1, -1, 0, 1 };
					int value = 0b0000_0000;
					int mask = 0b1000_0000;
					boolean valid = false;
					for (int k = 0; k < dX.length; k++) {
						System.out.println(String.format("Value: %s mask: %s ", Integer.toBinaryString(value), Integer.toBinaryString(mask)));
						if (getTypeAt(i + dX[k], j + dY[k]) == terrain) {
							value = value ^ mask;
							valid = true;
						}
						mask = mask >>> 1;
					}

					if (valid) {
						switch (value) {
						case 0b0000_0000:
							throw new IllegalStateException("Tile marked as valid but has no valid adjacent tiles");
						case 0b1000_0000: // top permutations
						case 0b1100_0000:
						case 0b1000_0001:
						case 0b1100_0001:
							setTypeAt(i, j, terrain + 16);
							break;
						case 0b0100_0000:
							setTypeAt(i, j, terrain + 15);
							break;
						case 0b0010_0000: // right permutations
						case 0b0110_0000:
						case 0b0011_0000:
						case 0b0111_0000:
							setTypeAt(i, j, terrain - 1);
							break;
						case 0b0001_0000:
							setTypeAt(i, j, terrain - 17);
							break;
						case 0b0000_1000: // down permutations
						case 0b0000_1100:
						case 0b0001_1000:
						case 0b0001_1100:
							setTypeAt(i, j, terrain - 16);
							break;
						case 0b0000_0100:
							setTypeAt(i, j, terrain - 15);
							break;
						case 0b0000_0010: // left permuatations
						case 0b0000_0011:
						case 0b0000_0110:
						case 0b0000_0111:
							setTypeAt(i, j, terrain + 1);
							break;
						case 0b0000_0001:
							setTypeAt(i, j, terrain + 17);
							break;
						case 0b1000_0011: // top left 3/4ths
						case 0b1000_0111:
						case 0b1100_0011:
						case 0b1100_0111:
							setTypeAt(i, j, terrain - 14);
						case 0b1110_0000: // top right 3/4ths
						case 0b1110_0001:
						case 0b1111_0000:
						case 0b1111_0001:
							setTypeAt(i, j, terrain - 13);
							break;
						case 0b0011_1000: // down right 3/4ths
						case 0b0011_1100:
						case 0b0111_1000:
						case 0b0111_1100:
							setTypeAt(i, j, terrain + 3);
							break;
						case 0b0000_1110: // down left 3/4ths
						case 0b0001_1110:
						case 0b0000_1111:
						case 0b0001_1111:
							setTypeAt(i, j, terrain + 2);
							break;

						}
						//case 0b1101_0000:
						//case 0b1000_0101:
						typeFlags[i][j] = true;
					}
				}
			}
		}
		//					setTypeAt(i + 1, j + 1, terrain - 15);
		//					setTypeAt(i - 1, j + 1, terrain - 17);
		//					setTypeAt(i + 1, j - 1, terrain + 17);
		//					setTypeAt(i - 1, j - 1, terrain + 15);

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
		if (tilesetDefinition == null) {
			return 0;
		}
		if (getTypeAt(i, j - 1) == tilesetDefinition.stairs) {
			return getTypeAt(i, j) == tilesetDefinition.stairs ? tilesetDefinition.stairs + 2 : tilesetDefinition.floor;
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
			return tilesetDefinition.edgeLeft;
		case 0b0010:
			return tilesetDefinition.edgeBottom;
		case 0b0100:
			return tilesetDefinition.edgeRight;
		case 0b1000:
			return tilesetDefinition.edgeTop;
		case 0b1100:
			return tilesetDefinition.edgeTopRight;
		case 0b1001:
			return tilesetDefinition.edgeTopLeft;
		case 0b0110:
			return tilesetDefinition.edgeBottomRight;
		case 0b0011:
			return tilesetDefinition.edgeBottomLeft;
		}
		//TODO: More cases (0101, 1010, 1111, etc)
		return getTypeAt(i, j);
	}

	private int createWall(int i, int j, int drop, int altitude, int baseAlt) {
		if (getTypeAt(i, j - 1) == tilesetDefinition.stairs) {
			return tilesetDefinition.stairs + 1;
		}
		if (drop == altitude - baseAlt) {
			// lower walls
			if (getAltitudeAt(i - 1, j) < altitude) {
				return tilesetDefinition.bottomLeftWall;
			} else if (getAltitudeAt(i + 1, j) < altitude) {
				return tilesetDefinition.bottomRightWall;
			} else {
				return tilesetDefinition.bottomCenterWall;
			}
		} else {
			// upper walls
			if (getAltitudeAt(i - 1, j) < altitude) {
				return tilesetDefinition.middleLeftWall;
			} else if (getAltitudeAt(i + 1, j) < altitude) {
				return tilesetDefinition.middleRightWall;
			} else {
				return tilesetDefinition.middleCenterWall;
			}
		}
	}

	@Override
	public void dispose() {
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
		try {
			x = MathUtils.clamp(x, 0, altMap.length - 1);
			y = MathUtils.clamp(y, 0, altMap[0].length - 1);
			return altMap[x][y];
		} catch (NullPointerException npe) {
			return 0;
		}
	}

	public TilesetDefinition getDefinition() {
		return tilesetDefinition;
	}

	/** @return the height (not altitude) of the map */
	public int getHeight() {
		return height;
	}

	/** @return the tallest altitude on this map */
	public int getMaxAltitude() {
		return maxAlt;
	}

	public String getName() {
		return name;
	}

	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		if (filename == null) {
			throw new NullPointerException("Tileset filename is null");
		}
		Array<TextureRegion> regions = new Array<TextureRegion>();
		//		if (Game.assets().containsAsset("tiles/" + filename)) {
		final Texture texture = Game.assets().get("tiles/" + filename, Texture.class);
		for (int i = 0; i < Math.pow(Config.tilesetWidth / Config.tileWidth, 2); i++) {
			final int x = (i * Config.tileWidth) % Config.tilesetWidth;
			final int y = (i / Config.tileWidth) * Config.tileWidth;
			final int w = Config.tileWidth;
			regions.add(new TextureRegion(texture, x, y, w, w));
		}
		//		}
		return regions;
	}

	/** Gets the terrain yupe at some tile coordinates and does bounds checking
	 * too!
	 * @param x x tile
	 * @param y y tile
	 * @return */
	public int getTypeAt(int x, int y) {
		// clamp to the size of the map so it's assumed tiles outside the map are the same as edge tiles
		try {
			x = MathUtils.clamp(x, 0, altMap.length - 1);
			y = MathUtils.clamp(y, 0, altMap[0].length - 1);
			return typeMap[x][y];
		} catch (NullPointerException npe) {
			return 0;
		}
	}

	public int getWidth() {
		return width;
	}

	/** @param x
	 * @param y
	 * @return <code>true</code> if the tile x,y is in bounds */
	public boolean isInBounds(int x, int y) {
		return x >= 0 && x < altMap.length && y >= 0 && y < altMap[0].length;
	}

	/** Renders every visible layer.
	 * @param row the map row to render, in tiles
	 * @param xOffset the camera offset in tiles, to ensure we only render tiles
	 *        visible in the x axis */
	public void render(int row, int xOffset) {
		final int tileSize = Config.tileWidth;
		if (layers == null) {
			return;
		}
		for (int k = 0; k < layers.size; k++) {
			int alt = layers.get(k).alt;
			final Tile[][] data = layers.get(k).data;
			if (row < 0 || row >= altMap[0].length) {
				return;
			}
			for (int i = xOffset, n = xOffset + Config.viewWidth / Config.tileWidth + 1; i < n; i++) {
				// prevents bottom rows from creeping up during rendering
				// TODO: make this generic. i think for alts > 0 it will break
				int rowRenderHeight = alt == 0 ? 0 : getAltitudeAt(i, row);
				for (int j = 0; j <= rowRenderHeight; j++) {
					try {
						if (i >= 0 && i < data.length && row >= 0 && row < data[i].length && data[i][row + j] != null) {
							Game.batch().draw(tiles.get(data[i][row + j].id), (i * tileSize), (row * tileSize) + j * tileSize, tileSize, tileSize);
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

	/** Given some tile coordinates, checks if they're valid and the tile type
	 * there has not been defined., and if so, applies newType to those
	 * coordinates.
	 * @param x
	 * @param y
	 * @param newType */
	private void setTypeAt(int x, int y, int newType) {
		if (x >= 0 && x < altMap.length && y >= 0 && y < altMap[0].length && typeMap[x][y] == 0) {
			typeMap[x][y] = newType;
		}
	}

	/** Updates the map
	 * @param delta the time since the last frame */
	public void update(float delta) {

	}
}
