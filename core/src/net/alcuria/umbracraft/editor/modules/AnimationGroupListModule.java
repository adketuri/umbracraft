package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

public class AnimationGroupListModule extends ListModule<AnimationGroupDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new AnimationGroupDefinition());
	}

	@Override
	public void create(AnimationGroupDefinition definition, Table content) {
		//		final SearchFilterWidget search = new SearchFilterWidget(suggestions);
		//		//rootDefinition.add(new AnimationDefinition());
		//		//save();
		PopulateConfig config = new PopulateConfig();
		ObjectMap<String, Array<String>> suggestions = new ObjectMap<String, Array<String>>();
		final FileHandle handle = Gdx.files.external("umbracraft/animations.json");
		if (handle.exists()) {
			Array<AnimationDefinition> anims = new Json().fromJson(AnimationListDefinition.class, handle).animations;
			Array<String> suggestionsStr = new Array<String>();
			for (AnimationDefinition anim : anims) {
				suggestionsStr.add(anim.name);
			}
			suggestions.put("down", suggestionsStr);
			suggestions.put("up", suggestionsStr);
		}
		config.cols = 1;
		config.suggestions = suggestions;
		populate(content, AnimationGroupDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "AnimationGroup";
	}

}
