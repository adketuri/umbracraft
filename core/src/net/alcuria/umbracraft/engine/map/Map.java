package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.App;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Map {
	private final int height;
	private final Array<Layer> layers;
	private final Array<TextureRegion> tiles;
	private final int width;

	public Map() {
		tiles = new Array<TextureRegion>();
		tiles.addAll(getRegions());
		width = 5;
		height = 3;
		layers = new Array<Layer>() {
			{
				add(new Layer(width, height));
			}
		};
	}

	/**
	 * Returns an array of texture regions loaded from the tileset
	 */
	private Array<TextureRegion> getRegions() {
		final Texture texture = App.assets().get("tiles/debug.png", Texture.class);
		Array<TextureRegion> regions = new Array<TextureRegion>();
		for (int i = 0; i < Math.pow(App.config().tilesetWidth / App.config().tileWidth, 2); i++) {
			regions.add(new TextureRegion(texture, (i * App.config().tileWidth) % App.config().tilesetWidth, (i * App.config().tileWidth) / App.config().tileWidth, App.config().tileWidth, App.config().tileWidth));
		}
		return regions;
	}

	public void render() {
		if (layers == null) {
			return;
		}
		for (int i = 0; i < layers.size; i++) {
			layers.get(i).render(tiles);
		}
	}

	public void update(float delta) {

	}
}
