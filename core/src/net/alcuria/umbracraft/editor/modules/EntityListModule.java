package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.component.ComponentDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;

/** A module to list out all our entities.
 * @author Andrew Keturi */
public class EntityListModule extends ListModule<EntityDefinition> {

	@Override
	public void addListItem() {
		rootDefinition.add(new EntityDefinition());
	}

	@Override
	public void create(EntityDefinition definition, Table content) {
		for (ComponentDefinition component : definition.components) {
			populate(content, ComponentDefinition.class, component, new PopulateConfig());
			WidgetUtils.divider(content, "blue");
		}
		content.add(new VisLabel("Add Component: "));
		content.add(list());
	}

	@Override
	public String getTitle() {
		return "Entities";
	}

	private Actor list() {
		VisList<String> list = new VisList<String>();
		return list;
	}

}
