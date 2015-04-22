package net.alcuria.umbracraft.engine.map;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;
import net.alcuria.umbracraft.mapgen.MapGenerator;

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
		width = gen.getWidth();
		height = gen.getHeight();
		layers = new Array<Layer>();
		layers.add(new Layer(gen, def));
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
			regions.add(new TextureRegion(texture, (i * Game.config().tileWidth) % Game.config().tilesetWidth, (i * Game.config().tileWidth) / Game.config().tileWidth, Game.config().tileWidth, Game.config().tileWidth));
		}
		return regions;
	}

	/** Renders every visible layer. */
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
