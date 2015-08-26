package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.MapListModule;
import net.alcuria.umbracraft.editor.modules.Module.PopulateConfig;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

/** A widget to display a map with options to edit it based on the
 * {@link EditMode}.
 * @author Andrew Keturi */
public class MapEditorWidget {

	/** Defines different edit modes for the {@link MapEditorWidget}. For
	 * example, in {@link EditMode#ALTITUDE}, clicking adjusts the altitude.
	 * @author Andrew Keturi */
	public static enum EditMode {
		ALTITUDE, ENTITY;

		@Override
		public String toString() {
			switch (this) {
			case ALTITUDE:
				return "Change Altitude";
			case ENTITY:
				return "Edit Entities";
			default:
				return "No EditMode";
			}
		};

	}

	private EditMode editMode = EditMode.ALTITUDE;
	private Listener listener;
	private final MapListModule module;
	private Table popupTable;

	public MapEditorWidget(MapListModule module) {
		this.module = module;
	}

	public void addPopulateListener(Listener listener) {
		this.listener = listener;
	}

	private Listener closeListener(final EntityReferenceDefinition entity) {
		return new Listener() {

			@Override
			public void invoke() {
				if (entity == null || entity.name.length() < 1) {
					module.getDefinition().entities.removeValue(entity, true);
				}
				popupTable.setVisible(false);
			}
		};
	}

	/** @return a new map widget, consisting of several {@link MapTileWidget}
	 *         classes to represent the current {@link MapDefinition}. */
	public Actor getActor() {
		return new Stack() {
			{
				add(new Table() {
					{
						for (int j = 0; j < module.getDefinition().getHeight(); j++) {
							Table row = new Table();
							for (int i = 0; i < module.getDefinition().getWidth(); i++) {
								row.add(new MapTileWidget(i, j, module.getDefinition(), MapEditorWidget.this)).size(32).pad(0);
							}
							add(row).row();
						}
					}
				});
				add(new Table() {
					{
						add(popupTable = new Table()).size(300);
					}
				});
			}
		};
	}

	/** @return the current {@link EditMode} */
	public EditMode getEditMode() {
		return editMode;
	}

	/** @return the {@link PopulateConfig} for showing the entity popup */
	private PopulateConfig populateConfig() {
		return new PopulateConfig() {
			{
				suggestions = new ObjectMap<String, Array<String>>();
				suggestions.put("name", new Array<String>() {
					{
						final FileHandle handle = Gdx.files.external("umbracraft/entities.json");
						if (handle.exists()) {
							Array<EntityDefinition> entities = new Json().fromJson(ListDefinition.class, handle).items();
							for (EntityDefinition entity : entities) {
								add(entity.name);
							}
						}
					}
				});
				cols = 1;
			}
		};
	}

	/** Sets the definition to use. Call this when it has updated elsewhere.
	 * (eg., a resize)
	 * @param definition the {@link MapDefinition} */
	//	public void setDefinition(MapDefinition definition) {
	//		this.definition = definition;
	//	}

	/** Sets the edit mode, which dictates the action to take when a tile is
	 * clicked.
	 * @param editMode the {@link EditMode} */
	public void setEditMode(EditMode editMode) {
		this.editMode = editMode;
	}

	/** Shows an entity popup for the tile at coordinates i,j
	 * @param i
	 * @param j */
	public void showEntityPopup(int i, int j) {
		popupTable.setVisible(true);
		popupTable.clear();
		popupTable.setBackground(Drawables.get("black"));
		EntityReferenceDefinition entity = module.getDefinition().findEntity(i, j);
		if (entity == null) {
			// populate with new data
			entity = new EntityReferenceDefinition();
			entity.x = i;
			entity.y = j;
			entity.name = "";
			module.getDefinition().entities.add(entity);
		}
		WidgetUtils.popupTitle(popupTable, "Add/Remove Entity " + entity.name, closeListener(entity));
		module.populate(popupTable, EntityReferenceDefinition.class, entity, populateConfig());
		popupTable.row();
		popupTable.add(WidgetUtils.button("Close", closeListener(entity)));
	}
}
