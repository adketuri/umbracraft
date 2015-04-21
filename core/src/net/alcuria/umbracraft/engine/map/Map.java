package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;
import net.alcuria.umbracraft.mapgen.MapGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class Map {
	private final int height;
	private final Array<Layer> layers;
	private final Array<TextureRegion> tiles;
	private final int width;

	public Map() {
		String filename = "";
		TilesetDefinition def = null;
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
		final MapGenerator gen = new MapGenerator();
		gen.generate();
		width = gen.getWidth();
		height = gen.getHeight();
		layers = new Array<Layer>();
		layers.add(new Layer(gen, def));
	}

	/** Returns an array of texture regions loaded from the tileset
	 * @param filename */
	private Array<TextureRegion> getRegions(String filename) {
		final Texture texture = App.assets().get("tiles/" + filename, Texture.class);
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
