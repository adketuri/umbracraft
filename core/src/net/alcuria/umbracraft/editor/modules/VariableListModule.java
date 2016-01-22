package net.alcuria.umbracraft.editor.modules;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class VariableListModule extends ListModule<VariableDefinition> {

	@Override
	public void addListItem() {
		final VariableDefinition item = new VariableDefinition();
		item.id = "Untitled " + rootDefinition.size();
		rootDefinition.add(item);
	}

	@Override
	public void create(VariableDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		config.textFieldWidth = 200;
		populate(content, VariableDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "Variables";
	}
}
