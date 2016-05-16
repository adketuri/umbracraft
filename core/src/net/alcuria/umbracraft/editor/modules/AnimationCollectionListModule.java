package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.Definition;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationCollectionDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A module to manage references to several {@link AnimationGroupDefinition}
 * objects for various types of poses (Walking, Jumping, etc.) These are grouped
 * together so an AnimationComponent can easily handle these in the engine.
 * @author Andrew Keturi */
public class AnimationCollectionListModule extends ListModule<AnimationCollectionDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new AnimationCollectionDefinition());

	}

	@Override
	public void create(AnimationCollectionDefinition definition, Table content) {
		PopulateConfig config = new PopulateConfig();
		ObjectMap<String, Array<String>> suggestions = new ObjectMap<String, Array<String>>();
		final FileHandle handle = Gdx.files.external("umbracraft/animationgroup.json");
		if (handle.exists()) {
			ListDefinition<?> anims = new Json().fromJson(ListDefinition.class, handle);
			Array<String> suggestionsStr = new Array<String>();
			for (Definition anim : anims.items().values()) {
				suggestionsStr.add(anim.getName());
			}
			suggestions.put("falling", suggestionsStr);
			suggestions.put("idle", suggestionsStr);
			suggestions.put("jumping", suggestionsStr);
			suggestions.put("running", suggestionsStr);
			suggestions.put("walking", suggestionsStr);
			suggestions.put("inspect", suggestionsStr);
			suggestions.put("template", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().spritePath));
		}
		config.cols = 1;
		config.textFieldWidth = 200;
		config.suggestions = suggestions;
		populate(content, AnimationCollectionDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "AnimationCollection";
	}

}
