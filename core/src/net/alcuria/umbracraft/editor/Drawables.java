package net.alcuria.umbracraft.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

/** A helper function that contains several {@link TextureRegionDrawable} objects
 * to use in the editor.
 * @author Andrew Keturi */
public class Drawables {

	public static class R {
		public static final String black = "black";
		public static final String blue = "blue";
		public static final String yellow = "yellow";
	}

	private static ObjectMap<String, TextureRegionDrawable> drawables;

	private static boolean initialized = false;

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
		throw new IllegalArgumentException("No drawable found with name: " + name);
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

		initialized = true;
	}

	private Drawables() {
	}

}
