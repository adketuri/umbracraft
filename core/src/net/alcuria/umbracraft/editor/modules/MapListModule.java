package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.widget.MapEditorWidget;
import net.alcuria.umbracraft.editor.widget.MapEditorWidget.EditMode;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

/** The map editor module.
 * @author Andrew Keturi */
public class MapListModule extends ListModule<MapDefinition> {

	private MapDefinition definition;
	private Table headerButtons, mapView;
	private MapEditorWidget mapWidget;
	private VisTextField widthField, heightField;
	private int zoom = 1;

	@Override
	public void addListItem() {
		final MapDefinition mapDef = new MapDefinition();
		mapDef.setWidth(10);
		mapDef.setHeight(10);
		mapDef.name = "Map " + rootDefinition.items().size;
		mapDef.createTiles();
		mapDef.entities = new Array<>();
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

	/** @return the {@link MapDefinition} */
	public MapDefinition getDefinition() {
		return definition;
	}

	@Override
	public String getTitle() {
		return "Map";
	}

	private void refreshHeader() {
		headerButtons.clear();
		headerButtons.add(new Table() {
			{
				// resize table
				defaults().width(50);
				add(new VisLabel("width"));
				add(widthField = new VisTextField(definition.getWidth() + ""));
				add(new VisLabel("height"));
				add(heightField = new VisTextField(definition.getHeight() + ""));
				add(new VisTextButton("Resize") {
					{
						addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								try {
									definition.resize(Integer.valueOf(widthField.getText()), Integer.valueOf(heightField.getText()));
									refreshMap();
								} catch (Exception e) {
									Game.log("Error parsing.");
								}
							};
						});
					}
				}).padLeft(10);
			}
		}).row();
		headerButtons.add(new Table() {
			{
				// mode table -- toggles between entity/altitude
				add(new VisLabel("Current Edit Mode:"));
				add(new VisTextButton(mapWidget != null ? mapWidget.getEditMode().toString() : EditMode.ALTITUDE.toString()) {
					{
						addListener(new ClickListener() {
							@Override
							public void clicked(InputEvent event, float x, float y) {
								mapWidget.setEditMode(mapWidget.getEditMode() == EditMode.ALTITUDE ? EditMode.ENTITY : EditMode.ALTITUDE);
								setText(mapWidget.getEditMode().toString());
							};
						});
					}
				}).padLeft(10);
			}
		}).row();
		headerButtons.add(new Table() {
			{
				add(new VisLabel("Zoom:"));
				final VisSelectBox<Integer> selectBox = new VisSelectBox<Integer>() {
					{
						final Array<Integer> list = new Array<Integer>();
						list.add(1);
						list.add(2);
						list.add(4);
						setItems(list);
					}
				};
				add(selectBox);
				add(WidgetUtils.button("Change Zoom", new Listener() {

					@Override
					public void invoke() {
						zoom = selectBox.getSelected();
						refreshMap();
					}
				}));
			}
		}).row();
		PopulateConfig config = new PopulateConfig();
		config.cols = 1;
		populate(headerButtons, MapDefinition.class, definition, config);
		headerButtons.row();
		headerButtons.add().expand().fill().height(60); //some padding for higher altitudes
	}

	/** Rebuilds a new {@link MapEditorWidget} */
	public void refreshMap() {
		mapWidget = new MapEditorWidget(this);
		mapView.clear();
		mapView.add(mapWidget.getActor(zoom));
		System.gc();
	}
}
