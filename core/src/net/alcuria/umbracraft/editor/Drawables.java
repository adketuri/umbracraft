package net.alcuria.umbracraft.editor;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.scripts.MessageScriptCommand.MessageEmotion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

/** A helper function that contains several {@link TextureRegionDrawable} objects
 * to use in the editor.
 * @author Andrew Keturi */
public class Drawables {

	private static ObjectMap<String, TextureRegionDrawable> drawables;
	private static boolean initialized = false;
	private static TextureAtlas skin, faces;

	/** Gets the skin {@link TextureAtlas}
	 * @param region the region in the TextureAtlas
	 * @return a {@link TextureAtlas} */
	public static TextureRegion faces(String name, MessageEmotion emotion) {
		if (!initialized) {
			init();
		}
		final String region = name.toLowerCase() + "/" + emotion.toString().toLowerCase();
		final AtlasRegion atlas = faces.findRegion(region);
		if (atlas == null) {
			throw new NullPointerException("Region not found: " + region + ". Regions Available: " + faces.getRegions());
		}
		return atlas;
	}

	/** Gets a drawable from the map
	 * @param name the name of the drawable
	 * @return the {@link TextureRegionDrawable} */
	public static TextureRegionDrawable get(String name) {
		if (!initialized) {
			init();
		}
		if (drawables.containsKey(name)) {
			return drawables.get(name);
		}
		throw new IllegalArgumentException("No drawable found with name: " + name + ". Drawables Available: " + drawables.toString());
	}

	/** Call to initialize all the drawables. */
	public static void init() {
		if (initialized) {
			return;
		}
		Texture texture = new Texture(Gdx.files.internal("editor/skin.png"));
		drawables = new ObjectMap<String, TextureRegionDrawable>();
		drawables.put("transparent", new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 0, 0)));
		drawables.put("black", new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 1, 1)));
		drawables.put("yellow", new TextureRegionDrawable(new TextureRegion(texture, 1, 0, 1, 1)));
		drawables.put("blue", new TextureRegionDrawable(new TextureRegion(texture, 2, 0, 1, 1)));
		drawables.put("entity", new TextureRegionDrawable(new TextureRegion(texture, 0, 1, 16, 16)));
		if (Game.assets() != null) {
			skin = Game.assets().get("skin/skin.atlas", TextureAtlas.class);
			faces = Game.assets().get("faces/faces.atlas", TextureAtlas.class);
		}
		initialized = true;
	}

	/** @return whether or not the {@link Drawables} are loaded, initialized, and
	 *         ready to be used. */
	public static boolean isInitialized() {
		return initialized;
	}

	public static NinePatchDrawable ninePatch(String region) {
		final NinePatch patch = skin.createPatch(region);
		if (patch == null) {
			throw new NullPointerException("9patch not found: " + region + ". Regions Available: " + skin.getRegions());
		}
		return new NinePatchDrawable(patch);
	}

	/** Gets the skin {@link TextureAtlas}
	 * @param region the region in the TextureAtlas
	 * @return a {@link TextureAtlas} */
	public static TextureRegion skin(String region) {
		if (!initialized) {
			init();
		}
		final AtlasRegion atlas = skin.findRegion(region);
		if (atlas == null) {
			throw new NullPointerException("Region not found: " + region + ". Regions Available: " + skin.getRegions());
		}
		return atlas;
	}

	/** Finds a texture in the atlas and returns a new
	 * {@link TextureRegionDrawable}. Because a new object is created, be
	 * mindful when using this repeatedly.
	 * @param region the texture region {@link String} from the atlas
	 * @return a new {@link TextureRegionDrawable} */
	public static TextureRegionDrawable texture(String region) {
		return new TextureRegionDrawable(skin(region));
	}

	private Drawables() {
	}

}
