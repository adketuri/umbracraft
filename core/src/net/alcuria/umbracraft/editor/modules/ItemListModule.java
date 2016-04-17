package net.alcuria.umbracraft.editor.modules;

import java.util.HashSet;

import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.listeners.TypeListener;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A module for displaying all items in the editor. Fields are filtered based on
 * the type of the item.
 * @author Andrew Keturi */
public class ItemListModule extends ListModule<ItemDefinition> {
	private Table content;
	private ItemDefinition definition;

	@Override
	public void addListItem() {
		final ItemDefinition item = new ItemDefinition();
		item.name = "Item " + rootDefinition.size();
		rootDefinition.add(item);
	}

	@Override
	public void create(ItemDefinition definition, Table content) {
		this.content = content;
		this.definition = definition;
		update();
	}

	@Override
	public String getTitle() {
		return "Items";
	}

	private void update() {
		content.clear();
		final PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		config.textFieldWidth = 200;
		config.listener = new TypeListener<String>() {

			@Override
			public void invoke(String field) {
				if (field.equals("type")) {
					update();
				}
			}
		};
		config.filter = new HashSet<String>();
		config.filter.add("description");
		config.filter.add("name");
		config.filter.add("price");
		config.filter.add("tag");
		config.filter.add("type");
		config.filter.add("weight");
		config.filter.add("icon");
		switch (definition.type) {
		case COLLECTIBLE:
			break;
		case CONSUMABLE:
			config.filter.add("hpRecovery");
			config.filter.add("hpRecoveryPercent");
			break;
		case EQUIPMENT:
			config.filter.add("equipType");
			break;
		case SCRIPT:
			break;
		default:
			break;

		}
		config.suggestions = new ObjectMap<String, Array<String>>();
		config.suggestions.put("icon", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().iconPath, false));
		populate(content, ItemDefinition.class, definition, config);
	}
}
