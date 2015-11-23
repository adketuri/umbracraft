package net.alcuria.umbracraft.editor;

import net.alcuria.umbracraft.Game;

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
	private static TextureAtlas skin;

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
		drawables.put("black", new TextureRegionDrawable(new TextureRegion(texture, 0, 0, 1, 1)));
		drawables.put("yellow", new TextureRegionDrawable(new TextureRegion(texture, 1, 0, 1, 1)));
		drawables.put("blue", new TextureRegionDrawable(new TextureRegion(texture, 2, 0, 1, 1)));
		if (Game.assets() != null) {
			skin = Game.assets().get("skin/skin.atlas", TextureAtlas.class);
		}
		initialized = true;
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

	private Drawables() {
	}

}
