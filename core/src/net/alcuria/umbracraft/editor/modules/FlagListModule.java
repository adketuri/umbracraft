package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.FlagDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** A module for creating flags (switches).
 * @author Andrew Keturi */
public class FlagListModule extends ListModule<FlagDefinition> {

	@Override
	public void addListItem() {
		final FlagDefinition item = new FlagDefinition();
		item.id = "Untitled " + rootDefinition.size();
		rootDefinition.add(item);

	}

	@Override
	public void create(FlagDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		config.textFieldWidth = 200;
		populate(content, FlagDefinition.class, definition, config);
	}

	@Override
	public String getTitle() {
		return "Flags";
	}

}
