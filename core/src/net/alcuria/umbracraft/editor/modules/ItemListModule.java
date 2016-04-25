package net.alcuria.umbracraft.editor.modules;

import java.util.HashSet;

import net.alcuria.umbracraft.definitions.items.ItemDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.TypeListener;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.kotcrab.vis.ui.widget.VisLabel;

/** A module for displaying all items in the editor. Fields are filtered based on
 * the type of the item.
 * @author Andrew Keturi */
public class ItemListModule extends ListModule<ItemDefinition> {
	private Table content, iconTable;
	private ItemDefinition definition;

	@Override
	public void addListItem() {
		final ItemDefinition item = new ItemDefinition();
		item.name = "Item " + rootDefinition.size();
		item.usageFilter = new Array<String>();
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
			config.filter.add("atk");
			config.filter.add("def");
			config.filter.add("matk");
			config.filter.add("mdef");
			config.filter.add("accuracy");
			config.filter.add("focus");
			config.filter.add("evasion");
			config.filter.add("critical");
			config.filter.add("speed");
			break;
		case SCRIPT:
			break;
		default:
			break;

		}
		config.suggestions = new ObjectMap<String, Array<String>>();
		config.suggestions.put("icon", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().iconPath, false));
		populate(content, ItemDefinition.class, definition, config);
		content.row();
		if (definition.usageFilter == null) {
			definition.usageFilter = new Array<String>();
		}
		content.row();
		content.add(new Table() {
			{
				add(WidgetUtils.tooltip("A list of players which can use this item. If empty, assumes all players can use it."));
				add(new VisLabel("usageFilter"));
				WidgetUtils.modifiableList(this, definition.usageFilter, Editor.db().heroes().keys());
			}
		}).row();
		content.add(iconTable = new Table()).row();
		updateIcon();
	}

	private void updateIcon() {
		iconTable.clear();
		String path = Editor.db().config().projectPath + Editor.db().config().iconPath + definition.icon + ".png";
		if (Gdx.files.absolute(path).exists()) {
			final Texture texture = new Texture(Gdx.files.absolute(path));
			iconTable.add(new Image(texture)).size(texture.getWidth() * 2, texture.getHeight() * 2);
		}
		Timer.schedule(new Task() {

			@Override
			public void run() {
				updateIcon(); // lazy af
			}
		}, 1);
	}
}
