package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.mapgen.MapGenerator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Layer {
	private final Tile[][] data;
	private final int width, height, altitude;

	/** This is for testing
	 * @param width
	 * @param height */
	public Layer(int width, int height) {
		this.width = width;
		this.height = height;
		altitude = 0;
		data = new Tile[width][height];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (i == 1 && j == 0) {
					data[i][j] = new Tile(19, false);
				} else {
					data[i][j] = new Tile(0, true);
				}
			}
		}
	}

	/** Creates a layer from a map generator
	 * @param gen */
	public Layer(MapGenerator gen, TilesetDefinition definition) {
		if (definition == null) {
			throw new NullPointerException("TilesetDefinition must not be null");
		} else if (gen == null) {
			throw new NullPointerException("MapGenerator must not be null");
		}
		width = gen.getWidth();
		height = gen.getHeight();
		altitude = 0;
		data = new Tile[width][height];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (!gen.isFilled(i, j)) {
					data[i][j] = new Tile(definition.bottomCenterWall, false);
				} else {
					data[i][j] = new Tile(definition.floor, true);
				}
			}
		}
	}

	public void render(Array<TextureRegion> tiles) {
		final int tileSize = App.config().tileWidth;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j] != null) {
					App.batch().draw(tiles.get(data[i][j].id), (i * tileSize), (j * -tileSize), tileSize, tileSize);
				}
			}
		}
	}
}
