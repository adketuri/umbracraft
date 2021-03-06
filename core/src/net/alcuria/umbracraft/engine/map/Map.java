package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Db;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** An internal representation of a playable, explorable map. It consists largely
 * of an array of {@link Layer} objects and an array of {@link TextureRegion}
 * objects to render the map.
 * @author Andrew Keturi */
public class Map implements Disposable {
	private static final boolean debugRenderMode = false;
	private static int staticRow;
	private static int timer;
	private int[][] altMap;
	private int height;
	private Array<Layer> layers;
	private MapDefinition mapDef;
	private int maxAlt;
	private String name;
	private AutoTileAttributes[][] overlayTypeMap, typeMap;
	private TilesetDefinition tilesetDefinition;

	private TileView tileView;
	private int width;

	/** Creates the map, building all layers and calculating terrain tiles so the
	 * map may be rendered.
	 * @param id the map's id from the {@link Db}. */
	public void create(String id) {
		if (id == null) {
			throw new NullPointerException("id cannot be null. Perhaps an area node's mapDefinition field is null?");
		}
		name = id;

		// create alt map from definition
		mapDef = Game.db().map(id);
		if (mapDef == null) {
			throw new NullPointerException("Map not found: " + id);
		}

		// load the tileset
		if (mapDef.tileset == null) {
			throw new NullPointerException("Map '" + id + "' must define a tileset");
		}
		tilesetDefinition = Game.db().tileset(mapDef.tileset);
		if (tilesetDefinition == null) {
			throw new NullPointerException("Cannot find tileset: " + mapDef.tileset);
		}
		String filename = tilesetDefinition.filename;

		width = mapDef.getWidth();
		height = mapDef.getHeight();
		altMap = new int[width][height];
		overlayTypeMap = new AutoTileAttributes[width][height];
		typeMap = new AutoTileAttributes[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				altMap[i][j] = mapDef.tiles.get(i).get(height - j - 1).altitude;

				// only initialize type indices where we know there is a terrain type
				if (mapDef.tiles.get(i).get(height - j - 1).type > 0) {
					typeMap[i][j] = new AutoTileAttributes(tilesetDefinition);
					typeMap[i][j].setType(mapDef.tiles.get(i).get(height - j - 1).type, false);
				}
				if (mapDef.tiles.get(i).get(height - j - 1).overlayType > 0) {
					overlayTypeMap[i][j] = new AutoTileAttributes(tilesetDefinition);
					overlayTypeMap[i][j].setType(mapDef.tiles.get(i).get(height - j - 1).overlayType, true);
				}

			}
		}

		// build map's terrains
		int[] terrains = { tilesetDefinition.terrain1, tilesetDefinition.terrain2, tilesetDefinition.overlay }; // this second terrain FUCKS UP my autotiles
		boolean[] isOverlay = { false, false, true };
		for (int k = 0; k < terrains.length; k++) {
			final int terrain = terrains[k];

			if (terrain > 0) {
				// go thru and set everything
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {

						//AK
						/** first, initialize the AutoTileAttributes as needed,
						 * for areas with nonzero types. Next, At this point we
						 * need to iterate thru the map and look at the type in
						 * the new AutoTileAttributes class. If it's non null,
						 * we need to look at the adjacent tiles in order to set
						 * some additional smart attributes for that tile
						 * (namely the four corners) */
						if ((!isOverlay[k] && typeMap[i][j] != null && typeMap[i][j].isInitialized()) || (isOverlay[k] && overlayTypeMap[i][j] != null && overlayTypeMap[i][j].isInitialized())) {
							// get surrounding mask
							// top topright right rightdown _ down downleft left lefttop
							int[] dX = { 0, 1, 1, 1, 0, -1, -1, -1 };
							int[] dY = { 1, 1, 0, -1, -1, -1, 0, 1 };
							int value = 0b0000_0000;
							int mask = 0b1000_0000;
							int alt = altMap[i][j];
							for (int l = 0; l < dX.length; l++) {
								final int typeAt = isOverlay[k] ? getOverlayTypeAt(i + dX[l], j + dY[l]) : getTypeAt(i + dX[l], j + dY[l]);
								if (typeAt == terrain) {// && alt == getAltitudeAt(i + dX[l], j + dY[l])) {
									value = value ^ mask;
								}
								mask = mask >>> 1;
							}

							setTypeAt(isOverlay[k] ? overlayTypeMap : typeMap, i, j, terrain, value);
						}
					}
				}
			}
		}

		// create tiles from definition
		tileView = new TileView(filename, tilesetDefinition);

		// create list of all altitudes
		Array<Integer> altitudes = new Array<Integer>();
		for (int i = 0; i < altMap.length; i++) {
			for (int j = 0; j < altMap[0].length; j++) {
				if (!altitudes.contains(Integer.valueOf(altMap[i][j]), false)) {
					altitudes.add(new Integer(altMap[i][j]));
				}
			}
		}
		altitudes.sort();
		maxAlt = altitudes.get(altitudes.size - 1);

		// build the layers
		layers = new Array<Layer>();
		for (Integer altitude : altitudes) {
			Layer layer = new Layer();
			layer.alt = altitude;
			layer.data = new Tile[width][height];
			for (int i = 0; i < altMap.length; i++) {
				for (int j = -getMaxAltitude(); j < altMap[0].length; j++) {
					if (getAltitudeAt(i, j) >= altitude) {
						if (isInBounds(i, j + altitude)) {
							layer.data[i][j + altitude] = new Tile(createEdge(i, j, altitude), layer.alt); // create edge
							final int overlay = getOverlayTypeAt(i, j);
							if (overlay == tilesetDefinition.overlayPiece1 || overlay == tilesetDefinition.overlayPiece2 || overlay == tilesetDefinition.overlayPiece3 || overlay == tilesetDefinition.overlayPiece4) { // 1 should be the forest overlay
								layer.data[i][j + altitude].overId = overlay;
							}
						}
						// check if we need to create a wall
						if (j - 1 >= 0 && j - 1 < altMap[0].length) {
							int drop = (altitude - getAltitudeAt(i, j - 1));
							while (drop > 0) {
								if (isInBounds(i, (j + altitude) - drop)) {
									if (getTypeAt(i, j) == tilesetDefinition.treeWall) {
										layer.data[i][(j + altitude) - drop] = new Tile(createTreeWall(i, j, drop, altitude), layer.alt);
									} else {
										layer.data[i][(j + altitude) - drop] = new Tile(createWall(i, j, drop, altitude, getAltitudeAt(i, j - 1)), layer.alt);
									}
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
		// we check if the altitude drops down OR we're adjacent to a tree and it's a drop down to the tree
		if (getAltitudeAt(i, j + 1) < altitude || (getTypeAt(i, j + 1) == tilesetDefinition.treeWall && getAltitudeAt(i, j + 1) - getAltitudeAt(i, j) < 4)) {
			mask = mask ^ 0b1000;
		}
		if (getAltitudeAt(i + 1, j) < altitude || (getTypeAt(i + 1, j) == tilesetDefinition.treeWall && getAltitudeAt(i + 1, j) - getAltitudeAt(i, j) < 4)) {
			mask = mask ^ 0b0100;
		}
		if (getAltitudeAt(i, j - 1) < altitude || (getTypeAt(i, j - 1) == tilesetDefinition.treeWall && getAltitudeAt(i, j - 1) - getAltitudeAt(i, j) < 4)) {
			mask = mask ^ 0b0010;
		}
		if (getAltitudeAt(i - 1, j) < altitude || (getTypeAt(i - 1, j) == tilesetDefinition.treeWall && getAltitudeAt(i - 1, j) - getAltitudeAt(i, j) < 4)) {
			mask = mask ^ 0b0001;
		}
		// now to switch on every possibility
		switch (mask) {
		case 0b0001:
			return tilesetDefinition.edge - 1;
		case 0b0010:
			return tilesetDefinition.edge + 16;
		case 0b0100:
			return tilesetDefinition.edge + 1;
		case 0b1000:
			return tilesetDefinition.edge - 16;
		case 0b1100:
			return tilesetDefinition.edge - 15;
		case 0b1001:
			return tilesetDefinition.edge - 17;
		case 0b0110:
			return tilesetDefinition.edge + 17;
		case 0b0011:
			return tilesetDefinition.edge + 15;
		}
		//TODO: More cases (0101, 1010, 1111, etc)
		return getTypeAt(i, j);
	}

	private int createTreeWall(int i, int j, int drop, int altitude) {
		Game.log(String.format("Create tree wall: i=%d j=%d drop=%d alt=%d", i, j, drop, altitude));
		int calculatedId;
		if (getAltitudeAt(i - 1, j) < altitude) {
			calculatedId = tilesetDefinition.treeWall + 1;
		} else if (getAltitudeAt(i + 1, j) < altitude) {
			calculatedId = tilesetDefinition.treeWall + 6;
		} else {
			int left = i;
			while (getAltitudeAt(left, j) == altitude && left > 0) {
				left--;
			}
			if ((i - left) % 2 == 0) {
				calculatedId = tilesetDefinition.treeWall + 2;
			} else {
				calculatedId = tilesetDefinition.treeWall + 3;
			}
		}
		calculatedId = calculatedId - ((4 - drop) * (Config.tilesetWidth / Config.tileWidth));
		return calculatedId;
	}

	private int createWall(int i, int j, int drop, int altitude, int baseAlt) {
		if (getTypeAt(i, j - 1) == tilesetDefinition.stairs) {
			return tilesetDefinition.stairs + 1;
		}

		if (tilesetDefinition.legacyWalls) {
			if (drop == altitude - baseAlt) {
				// lower walls
				if (getAltitudeAt(i - 1, j) < altitude || getTypeAt(i - 1, j) == tilesetDefinition.treeWall) {
					return tilesetDefinition.wall - 1;
				} else if (getAltitudeAt(i + 1, j) < altitude || getTypeAt(i + 1, j) == tilesetDefinition.treeWall) {
					return tilesetDefinition.wall + 1;
				} else {
					return tilesetDefinition.wall;
				}
			} else {
				// upper walls
				if (getAltitudeAt(i - 1, j) < altitude || getTypeAt(i - 1, j) == tilesetDefinition.treeWall) {
					return tilesetDefinition.wall - 17;
				} else if (getAltitudeAt(i + 1, j) < altitude || getTypeAt(i + 1, j) == tilesetDefinition.treeWall) {
					return tilesetDefinition.wall - 15;
				} else {
					return tilesetDefinition.wall - 16;
				}
			}
		} else {
			final int cols = Config.tilesetWidth / Config.tileWidth;
			int wall = cols * tilesetDefinition.wallHeight - (Math.min(drop, tilesetDefinition.wallHeight)) * cols;
			if (altitude - baseAlt == 1) {
				wall = 0; // this is a hack to fix cliffs of height 1, we should in general start with the bottom and work up but...
			}
			if (getAltitudeAt(i - 1, j) < altitude || getTypeAt(i - 1, j) == tilesetDefinition.treeWall) {
				return tilesetDefinition.wall - wall - 1;
			} else if (getAltitudeAt(i + 1, j) < altitude || getTypeAt(i + 1, j) == tilesetDefinition.treeWall) {
				return tilesetDefinition.wall - wall + 1;
			} else {
				return tilesetDefinition.wall - wall;
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

	/** Gets the altitude, in tiles, at some tile coordinates.
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

	/** @return A rectangle to use as the bounds of the map. */
	public Rectangle getBounds() {
		return new Rectangle(0, mapDef.bottomClamp * Config.tileWidth, Game.map().getWidth() * Config.tileWidth, Game.map().getHeight() * Config.tileWidth);
	}

	/** @return the current {@link TilesetDefinition} */
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

	/** @return the name of the map */
	public String getName() {
		return name;
	}

	/** Gets the overlay type at some tile coordinates and does bounds checking
	 * too!
	 * @param x x tile
	 * @param y y tile
	 * @return */
	public int getOverlayTypeAt(int x, int y) {
		if (x >= 0 && x < altMap.length && y >= 0 && y < altMap[0].length && overlayTypeMap[x][y] != null) {
			return overlayTypeMap[x][y].getType();
		}
		return 0;
	}

	/** Gets the terrain type at some tile coordinates and does bounds checking
	 * too!
	 * @param x x tile
	 * @param y y tile
	 * @return */
	public int getTypeAt(int x, int y) {
		// clamp to the size of the map so it's assumed tiles outside the map are the same as edge tiles
		try {
			x = MathUtils.clamp(x, 0, altMap.length - 1);
			y = MathUtils.clamp(y, 0, altMap[0].length - 1);
			return typeMap[x][y].getType();
		} catch (NullPointerException npe) {
			return 0;
		}
	}

	private int getWaterDefinition(int x, int y, int alt) {
		if (getAltitudeAt(x, y + 1) > alt) {
			if (getAltitudeAt(x - 1, y + 1) <= alt) {
				return tilesetDefinition.water + 1;
			} else if (getAltitudeAt(x + 1, y + 1) <= alt) {
				return tilesetDefinition.water + 3;
			}
			return tilesetDefinition.water + 2;
		}
		return tilesetDefinition.water;
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

	/** @param x the x coordinate, in tiles
	 * @param y the y coordinate, in tiles
	 * @return <code>true</code> if the coordinates contain stairs */
	public boolean isStairs(int x, int y) {
		return getTypeAt(x, y) == tilesetDefinition.stairs;
	}

	/** Renders every visible layer.
	 * @param row the map row to render, in tiles
	 * @param xOffset the camera offset in tiles, to ensure we only render tiles
	 *        visible in the x axis */
	public void render(int row, int xOffset) {
		if (debugRenderMode) {
			timer = (timer + 1) % 600;
			if (timer == 0) {
				Game.log(staticRow + "");
				staticRow = (staticRow + 1) % 10;
			}
			row = staticRow;
		}
		final int tileSize = Config.tileWidth;
		if (layers == null) {
			return;
		}
		for (int i = xOffset, n = xOffset + Config.viewWidth / Config.tileWidth + 1; i < n; i++) {
			int alt = getAltitudeAt(i, row);
			Tile[][] data = null;
			// we need to get the data for the tiles at this altitude
			// FIXME: no looping please
			for (int k = 0; k < layers.size; k++) {
				if (layers.get(k).alt == alt) {
					data = layers.get(k).data;
					break;
				}
			}
			if (data == null || row < 0 || row >= altMap[0].length) {
				return;
			}
			// prevents bottom rows from creeping up during rendering
			int drop = alt - getAltitudeAt(i, row + 1); // this works, but why?
			for (int j = alt; j >= drop; j--) {
				try {
					if (i >= 0 && i < data.length && row >= 0 && row < data[i].length && data[i][row + j] != null) {
						// if we have a special overlay here draw it
						if (typeMap[i][row] != null && typeMap[i][row].getType() != tilesetDefinition.stairs) {
							tileView.draw(typeMap[i][row], i * tileSize, row * tileSize + alt * tileSize);
						} else {
							// dont draw pink
							Game.batch().draw(tileView.get(data[i][row + j].id), (i * tileSize), (row * tileSize) + j * tileSize, tileSize, tileSize);
						}
						if (data[i][row + j].overId > 0) {
							Game.batch().draw(tileView.get(data[i][row + j].overId), (i * tileSize), (row * tileSize) + j * tileSize, tileSize, tileSize);
						}
					}
				} catch (ArrayIndexOutOfBoundsException | NullPointerException e2) {
					//FIXME: Halp. someting up with rendering very top and very bottom rows.
					//Game.log("render oob " + i + " " + j + " " + row);
				}
			}
			if (mapDef.waterLevel > alt || mapDef.waterLevel > getAltitudeAt(i, row + 1)) {
				//FIXME: kinda hacky, will need to rewrite rendering at some point to remove the concept of a "layer" -- its making things more complicated than they need to
				final int waterDefinition = getWaterDefinition(i, row, alt);
				if (waterDefinition > tilesetDefinition.water) {
					Game.batch().draw(tileView.get(waterDefinition), (i * tileSize), ((row + 1) * tileSize + mapDef.waterLevel * tileSize), tileSize, tileSize);
				}
				Game.batch().draw(tileView.get(tilesetDefinition.water), (i * tileSize), (row * tileSize + mapDef.waterLevel * tileSize), tileSize, tileSize);
			}
		}
	}

	public void renderOverlays(int xOffset, int yOffset) {
		final int tileSize = Config.tileWidth;
		for (int i = xOffset, n = xOffset + Config.viewWidth / Config.tileWidth + 1; i < n; i++) {
			for (int j = yOffset, m = yOffset + Config.viewWidth / Config.tileWidth + 1; j < m; j++) {
				final int overlayId = getOverlayTypeAt(i, j);
				if (overlayId > 0) {
					tileView.draw(overlayTypeMap[i][j], i * tileSize, j * tileSize + mapDef.overlayHeight * tileSize);
					if (j == 0) {
						// draw down
						for (int k = 1; k < 5; k++) {
							Game.batch().draw(tileView.get(overlayId), (i * tileSize), ((j - k) * tileSize) + mapDef.overlayHeight * tileSize, tileSize, tileSize);
						}
					}
				}
			}
		}

	}

	/** Given some tile coordinates, checks if they're valid and the tile type
	 * there has not been defined., and if so, applies newType to those
	 * coordinates.
	 * @param x
	 * @param y
	 * @param newType
	 * @param neighborMask */
	private void setTypeAt(AutoTileAttributes[][] attribs, int x, int y, int newType, int neighborMask) {
		if (x >= 0 && x < altMap.length && y >= 0 && y < altMap[0].length && attribs[x][y] != null) {
			attribs[x][y].setMask(neighborMask);
		}
	}

	/** Updates the map
	 * @param delta the time since the last frame */
	public void update(float delta) {

	}
}
