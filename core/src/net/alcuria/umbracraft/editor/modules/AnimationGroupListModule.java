package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A module to handle groups of animations for the eight directions.
 * @author Andrew Keturi */
public class AnimationGroupListModule extends ListModule<AnimationGroupDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new AnimationGroupDefinition());
	}

	@Override
	public void create(AnimationGroupDefinition definition, Table content) {
		PopulateConfig config = new PopulateConfig();
		ObjectMap<String, Array<String>> suggestions = new ObjectMap<String, Array<String>>();
		final FileHandle handle = Gdx.files.external("umbracraft/animations.json");
		if (handle.exists()) {
			ObjectMap<String, AnimationDefinition> anims = new Json().fromJson(AnimationListDefinition.class, handle).animations;
			Array<String> suggestionsStr = new Array<String>();
			for (AnimationDefinition anim : anims.values()) {
				suggestionsStr.add(anim.name);
			}
			suggestions.put("down", suggestionsStr);
			suggestions.put("up", suggestionsStr);
			suggestions.put("right", suggestionsStr);
			suggestions.put("left", suggestionsStr);
			suggestions.put("downLeft", suggestionsStr);
			suggestions.put("downRight", suggestionsStr);
			suggestions.put("upLeft", suggestionsStr);
			suggestions.put("upRight", suggestionsStr);
			suggestions.put("template", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().spritePath));
		}
		config.cols = 1;
		config.textFieldWidth = 200;
		config.suggestions = suggestions;
		populate(content, AnimationGroupDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "AnimationGroup";
	}

}
