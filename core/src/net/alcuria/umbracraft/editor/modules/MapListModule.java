package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.map.MapDefinition;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The map editor module.
 * @author Andrew Keturi */
public class MapListModule extends ListModule<MapDefinition> {

	private MapDefinition definition;
	private Table headerButtons, mapView;

	@Override
	public void addListItem() {
		rootDefinition.add(new MapDefinition());
	}

	@Override
	public void create(MapDefinition definition, Table content) {
		this.definition = definition;
		content.add(headerButtons = new Table()).expandX().fill().row();
		content.add(mapView = new Table()).expand().fill().row();
		refreshHeader();
		refreshMap();
	}

	@Override
	public String getTitle() {
		return "Map";
	}

	private void refreshHeader() {
		headerButtons.clear();
		headerButtons.add(new VisTextButton("Button")).row();
		PopulateConfig config = new PopulateConfig();
		config.cols = 10;
		populate(headerButtons, MapDefinition.class, definition, config);
	}

	private void refreshMap() {
		mapView.clear();
	}

}
