package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.anim.AnimationGroupDefinition;
import net.alcuria.umbracraft.editor.widget.SearchFilterWidget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;

public class AnimationGroupListModule extends ListModule<AnimationGroupDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new AnimationGroupDefinition());
	}

	@Override
	public void create(AnimationGroupDefinition definition, Table content) {
		content.add(new VisLabel("Hello from the grouplistmodule " + ((definition != null && definition.getName() != null) ? definition.getName() : "null")));
		Array<String> suggestions = new Array<String>();
		suggestions.add("Amiru");
		suggestions.add("Amethyst");
		suggestions.add("Andrew");
		suggestions.add("America");
		final SearchFilterWidget search = new SearchFilterWidget(suggestions);
		//		//rootDefinition.add(new AnimationDefinition());
		//		//save();
	}

	@Override
	public String getTitle() {
		return "AnimationGroup";
	}

}
