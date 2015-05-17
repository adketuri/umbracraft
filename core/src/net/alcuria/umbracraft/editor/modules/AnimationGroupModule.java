package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.definitions.anim.AnimationGroupListDefinition;
import net.alcuria.umbracraft.editor.widget.SearchFilterWidget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class AnimationGroupModule extends Module<AnimationGroupListDefinition> {

	private final Table view;

	public AnimationGroupModule() {
		super();
		load(AnimationGroupListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new AnimationGroupListDefinition();
		}
		view = new Table();
	}

	@Override
	public String getTitle() {
		return "AnimationGroups";
	}

	@Override
	public void populate(Table content) {
		Table menu = new Table();

	}

	private void setMainContent(Table view, AnimationGroupDefinition definition) {
		Array<String> suggestions = new Array<String>();
		suggestions.add("Amiru");
		suggestions.add("Amethyst");
		suggestions.add("Andrew");
		suggestions.add("America");
		SearchFilterWidget search = new SearchFilterWidget(suggestions);
		view.add(search.getActor()).row();
	}
}
