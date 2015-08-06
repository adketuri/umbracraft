package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.widget.MapEditorWidget;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

/** The map editor module.
 * @author Andrew Keturi */
public class MapListModule extends ListModule<MapDefinition> {

	private MapDefinition definition;
	private Table headerButtons, mapView;
	private MapEditorWidget mapWidget;
	private VisTextField widthField, heightField;

	@Override
	public void addListItem() {
		final MapDefinition mapDef = new MapDefinition();
		mapDef.setWidth(10);
		mapDef.setHeight(10);
		mapDef.name = "New Map";
		mapDef.createTiles();
		rootDefinition.add(mapDef);
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
		headerButtons.add(widthField = new VisTextField(definition.getWidth() + ""));
		headerButtons.add(heightField = new VisTextField(definition.getHeight() + ""));
		headerButtons.add(new VisTextButton("Resize") {
			{
				addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						try {
							definition.resize(Integer.valueOf(widthField.getText()), Integer.valueOf(heightField.getText()));
							mapWidget.setDefinition(definition);
						} catch (Exception e) {
							Game.log("Error parsing.");
						}
					};
				});
			}
		}).row();
		PopulateConfig config = new PopulateConfig();
		config.cols = 10;
		populate(headerButtons, MapDefinition.class, definition, config);
	}

	private void refreshMap() {
		mapWidget = new MapEditorWidget(definition);
		mapView.clear();
		mapView.add(mapWidget.getActor());
	}

}
