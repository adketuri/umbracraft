package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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

	private MapDefinition definition;
	private EditMode editMode = EditMode.ALTITUDE;
	private Table popupTable;

	public MapEditorWidget(MapDefinition definition) {
		this.definition = definition;
	}

	private Listener closeListener() {
		return new Listener() {

			@Override
			public void invoke() {
				popupTable.clear();
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
						for (int j = 0; j < definition.getHeight(); j++) {
							Table row = new Table();
							for (int i = 0; i < definition.getWidth(); i++) {
								row.add(new MapTileWidget(i, j, definition, MapEditorWidget.this)).size(32).pad(0);
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

	/** Sets the definition to use. Call this when it has updated elsewhere.
	 * (eg., a resize)
	 * @param definition the {@link MapDefinition} */
	public void setDefinition(MapDefinition definition) {
		this.definition = definition;
	}

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
		popupTable.clear();
		popupTable.setBackground(Drawables.get("black"));
		WidgetUtils.popupTitle(popupTable, "Add/Remove Entity", closeListener());
		EntityDefinition entity = definition.findEntity(i, j);
		if (entity != null) {
			// populate with entity data
		}
	}

}
