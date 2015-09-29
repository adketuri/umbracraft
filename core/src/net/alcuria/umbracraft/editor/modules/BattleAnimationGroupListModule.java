package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationListDefinition;
import net.alcuria.umbracraft.definitions.anim.BattleAnimationGroupDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A module to handle groups of animations for battles.
 * @author Andrew Keturi */
public class BattleAnimationGroupListModule extends ListModule<BattleAnimationGroupDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new BattleAnimationGroupDefinition());
	}

	@Override
	public void create(BattleAnimationGroupDefinition definition, Table content) {
		PopulateConfig config = new PopulateConfig();
		ObjectMap<String, Array<String>> suggestions = new ObjectMap<String, Array<String>>();
		final FileHandle handle = Gdx.files.external("umbracraft/battle_animations.json");
		if (handle.exists()) {
			ObjectMap<String, AnimationDefinition> anims = new Json().fromJson(AnimationListDefinition.class, handle).animations;
			Array<String> suggestionsStr = new Array<String>();
			for (AnimationDefinition anim : anims.values()) {
				suggestionsStr.add(anim.name);
			}
			suggestions.put("idle", suggestionsStr);
			suggestions.put("attack", suggestionsStr);
			suggestions.put("hurt", suggestionsStr);
			suggestions.put("dead", suggestionsStr);
			suggestions.put("towards", suggestionsStr);
			suggestions.put("away", suggestionsStr);
		}
		config.cols = 1;
		config.textFieldWidth = 200;
		config.suggestions = suggestions;
		populate(content, BattleAnimationGroupDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "BattleAnimationGroup";
	}

}
