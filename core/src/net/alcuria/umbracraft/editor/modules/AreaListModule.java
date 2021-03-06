package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.area.AreaDefinition;
import net.alcuria.umbracraft.definitions.area.AreaNodeDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget;
import net.alcuria.umbracraft.editor.widget.AreaNodeWidget.NodeClickHandler;
import net.alcuria.umbracraft.editor.widget.TeleportSelectorWidget;
import net.alcuria.umbracraft.editor.widget.WidgetUtils;
import net.alcuria.umbracraft.listeners.Listener;

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
		refresh();
	}

	@Override
	public void addListItem() {
		final AreaDefinition area = new AreaDefinition();
		area.name = "Area " + (rootDefinition.size() + 1);
		rootDefinition.add(area);
	}

	@Override
	public void create(final AreaDefinition definition, Table content) {
		if (definition.root == null) {
			definition.root = new AreaNodeDefinition();
			definition.root.name = "Root";
			definition.root.children = new Array<AreaNodeDefinition>();
		}
		content.add(new Table() {
			{
				populate(this, AreaDefinition.class, definition, new PopulateConfig());
			}
		}).row();
		areaDefinition = definition;
		AreaNodeWidget area = new AreaNodeWidget(definition.root, this);
		widgetTable = new Table();
		widgetTable.add(area).expand().fill();
		content.stack(widgetTable, popupTable = new Table()).expand().fill().row();
		content.add(new Table() {
			{
				add(new Table() {
					{
						add(WidgetUtils.tooltip("A list of entities present on this area."));
						add(new VisLabel("Area Entities:"));
					}
				}).row();
				WidgetUtils.divider(this, "blue");
				WidgetUtils.modifiableList(this, definition.entities, new Array<String>(Editor.db().entities().keys()));
			}
		});
	}

	private Listener deleteListener(final AreaNodeDefinition definition) {
		return new Listener() {

			@Override
			public void invoke() {
				areaDefinition.deleteNode(areaDefinition.root, definition);
				refresh();
			}
		};
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
				WidgetUtils.popupTitle(this, "Editing " + definition.getName(), popupCloseListener());
				// content
				add(new Table() {
					{
						PopulateConfig config = new PopulateConfig();
						config.cols = 1;
						config.suggestions = new ObjectMap<String, Array<String>>();
						config.suggestions.put("mapDefinition", new Array<String>() {
							{
								final FileHandle handle = Gdx.files.external("umbracraft/map.json");
								if (handle.exists()) {
									ObjectMap<String, MapDefinition> maps = new Json().fromJson(ListDefinition.class, handle).items();
									for (MapDefinition map : maps.values()) {
										add(map.name);
									}
								}
							}
						});
						populate(this, AreaNodeDefinition.class, definition, config);
						row();
						add(new TeleportSelectorWidget(definition).getActor());
					}
				}).expand().fill().row();
				add(new Table() {
					{
						defaults().pad(10);
						// "add child" button
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
						// delete
						add(WidgetUtils.button("Delete", deleteListener(definition)));
					}

				});
			}

		}).expand().fill().size(500, 500);
	};

	private Listener popupCloseListener() {
		return new Listener() {

			@Override
			public void invoke() {
				popupTable.clear();
				widgetTable.clear();
				widgetTable.add(new AreaNodeWidget(areaDefinition.root, AreaListModule.this)).expand().fill();
			}
		};
	}

	private void refresh() {
		widgetTable.clear();
		if (areaDefinition.root == null) {
			areaDefinition.root = new AreaNodeDefinition();
		}
		widgetTable.add(new AreaNodeWidget(areaDefinition.root, this)).expand().fill();
		popupTable.clear();
	}
}
