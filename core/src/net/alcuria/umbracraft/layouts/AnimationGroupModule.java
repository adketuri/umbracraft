package net.alcuria.umbracraft.layouts;

import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.modules.Module;
import net.alcuria.umbracraft.widget.SearchFilterWidget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class AnimationGroupModule extends Module<AnimationGroupDefinition> {

	@Override
	public String getTitle() {
		return "AnimationGroups";
	}

	@Override
	public void populate(Table content) {
		Array<String> suggestions = new Array<String>();
		suggestions.add("Amiru");
		suggestions.add("Amethyst");
		suggestions.add("Andrew");
		suggestions.add("America");
		SearchFilterWidget search = new SearchFilterWidget(suggestions);

		content.add(search.getActor()).row();
	}

}
