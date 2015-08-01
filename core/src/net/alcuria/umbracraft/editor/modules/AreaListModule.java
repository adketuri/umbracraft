package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget.NodeClickHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The AreaListModule handles displaying UI for all the {@link AreaDefinition}
 * classes within the editor. The layout consists of a tree display of all the
 * {@link AreaNodeDefinition} classes in the form of an {@link AreaNodeWidget}
 * and a popup that appears over the widget when nodes are clicked to edit node
 * properties.
 * @author Andrew Keturi */
public class AreaListModule extends ListModule<AreaDefinition> implements NodeClickHandler {

	private AreaDefinition areaDefinition; // keep around a reference to the top-level area definition to make rebuilding trees possible
	private Table popupTable, widgetTable; // containers for the widget and popup

	/** adds a child node to the current definition and rebuilds the
	 * {@link AreaNodeWidget}
	 * @param definition the definition to which the child is added */
	private void addChild(final AreaNodeDefinition definition) {
		if (definition.children == null) {
			definition.children = new Array<AreaNodeDefinition>();
		}
		AreaNodeDefinition child = new AreaNodeDefinition();
		child.name = definition.getName() + " Child";
		definition.children.add(child);
		widgetTable.clear();
		widgetTable.add(new AreaNodeWidget(areaDefinition.root, this)).expand().fill();
		popupTable.clear();
	}

	@Override
	public void addListItem() {
		rootDefinition.add(new AreaDefinition());
	}

	@Override
	public void create(AreaDefinition definition, Table content) {
		if (definition.root == null) {
			definition.root = new AreaNodeDefinition();
			definition.root.name = "Root";
			definition.root.children = new Array<AreaNodeDefinition>();
		}
		areaDefinition = definition;
		AreaNodeWidget area = new AreaNodeWidget(definition.root, this);
		widgetTable = new Table();
		widgetTable.add(area).expand().fill();
		content.stack(widgetTable, popupTable = new Table()).expand().fill();
	}

	@Override
	public String getTitle() {
		return "Areas";
	}

	@Override
	public void onClick(final AreaNodeDefinition definition) {
		popupTable.clear();
		popupTable.add(new Table() {
			{
				setBackground(Drawables.get("black"));
				// title
				add(new Table() {
					{
						setBackground(Drawables.get("blue"));
						add(new VisTextButton("X") {
							{
								addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										popupTable.clear();
										widgetTable.clear();
										widgetTable.add(new AreaNodeWidget(areaDefinition.root, AreaListModule.this)).expand().fill();
									};
								});
							}
						});
						add(new VisLabel("Editing " + definition.getName())).expand().center();
					}
				}).expand().fillX().top().row();
				// content
				add(new Table() {
					{
						PopulateConfig config = new PopulateConfig();
						config.cols = 2;
						config.suggestions = new ObjectMap<String, Array<String>>();
						config.suggestions.put("mapDefinition", new Array<String>() {
							{
								final FileHandle handle = Gdx.files.external("umbracraft/map.json");
								if (handle.exists()) {
									Array<MapDefinition> maps = new Json().fromJson(ListDefinition.class, handle).items();
									for (MapDefinition map : maps) {
										add(map.name);
									}
								}
							}
						});
						populate(this, AreaNodeDefinition.class, definition, config);
					}
				}).expand().fill().row();
				// "add child" button
				add(new Table() {
					{
						add(new VisTextButton("Add Child") {
							{
								addListener(new ClickListener() {
									@Override
									public void clicked(InputEvent event, float x, float y) {
										addChild(definition);
									}

								});
							}
						});
					}
				});
			}
		}).expand().fill().size(500, 500);
	};
}
