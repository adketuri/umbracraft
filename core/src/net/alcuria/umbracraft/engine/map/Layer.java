package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.App;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Layer {
	private final Tile[][] data;
	private final int width, height;

	public Layer(int width, int height) {
		this.width = width;
		this.height = height;
		data = new Tile[width][height];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (i == 1 && j == 0) {
					data[i][j] = new Tile(19);
				} else {
					data[i][j] = new Tile(0);
				}
			}
		}
	}

	public void render(Array<TextureRegion> tiles) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				if (data[i][j] != null) {
					App.batch().draw(tiles.get(data[i][j].id), (i * 16), (j * -16), 16, 16);
				}
			}
		}
	}
}
