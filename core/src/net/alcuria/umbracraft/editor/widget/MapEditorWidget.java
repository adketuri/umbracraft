package net.alcuria.umbracraft.editor.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.map.MapTileDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.modules.MapListModule;
import net.alcuria.umbracraft.editor.modules.Module.PopulateConfig;
import net.alcuria.umbracraft.listeners.Listener;
import net.dermetfan.utils.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.kotcrab.vis.ui.widget.VisLabel;

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

	private static EditMode editMode = EditMode.ALTITUDE;

	private static Pair<Vector2, MapTileDefinition> tile(final int x, final int y, final MapTileDefinition tile) {
		return new Pair<Vector2, MapTileDefinition>(new Vector2(x, y), tile);
	}

	private Actor actor;
	private Label coordinates;
	private boolean entered;
	private Listener listener;
	private final MapListModule module;
	private Table popupTable, helpTable;
	private final Array<MapTileWidget> tiles = new Array<MapTileWidget>();
	private int zoom;

	public MapEditorWidget(MapListModule module) {
		this.module = module;
	}

	private Listener closeListener(final EntityReferenceDefinition entity) {
		return new Listener() {

			@Override
			public void invoke() {
				if (entity == null || entity.name.length() < 1) {
					module.getDefinition().entities.removeValue(entity, true);
				} else {
					module.refreshMap();
				}
				popupTable.setVisible(false);
			}
		};
	}

	private Listener deleteListener(final EntityReferenceDefinition entity) {
		return new Listener() {

			@Override
			public void invoke() {
				module.getDefinition().entities.removeValue(entity, true);
				popupTable.setVisible(false);
				module.refreshMap();
			}
		};
	}

	/** Fills tiles up to +1/-1 altitude
	 * @param x the x tile coordinate
	 * @param y the y tile coordinate
	 * @param increase whether to increase or decrease the fill level */
	private void fill(int x, int y, boolean increase) {
		final MapDefinition map = module.getDefinition();
		final MapTileDefinition tile = map.getTileDefinition(x, y);
		final int currentAltitude = tile.altitude;
		final int targetAltitude = increase ? currentAltitude + 1 : currentAltitude - 1;
		final List<Pair<Vector2, MapTileDefinition>> tilesToFill = new ArrayList<Pair<Vector2, MapTileDefinition>>();
		final Set<MapTileDefinition> tilesInList = new HashSet<MapTileDefinition>();

		tilesToFill.add(tile(x, y, tile));
		tilesInList.add(tile);

		while (!tilesToFill.isEmpty()) {
			final Pair<Vector2, MapTileDefinition> currentTile = tilesToFill.remove(tilesToFill.size() - 1);
			final MapTileDefinition currentMapDefinition = currentTile.getValue();
			tilesInList.remove(currentMapDefinition);

			if (currentMapDefinition.altitude != currentAltitude) {
				continue;
			}

			currentMapDefinition.altitude = targetAltitude;

			final int currentX = (int) currentTile.getKey().x;
			final int currentY = (int) currentTile.getKey().y;

			final MapTileDefinition left = map.getTileDefinition(currentX - 1, currentY);
			if (left != null && !tilesInList.contains(left)) {
				tilesToFill.add(tile(currentX - 1, currentY, left));
				tilesInList.add(left);
			}

			final MapTileDefinition right = map.getTileDefinition(currentX + 1, currentY);
			if (right != null && !tilesInList.contains(right)) {
				tilesToFill.add(tile(currentX + 1, currentY, right));
				tilesInList.add(right);
			}

			final MapTileDefinition top = map.getTileDefinition(currentX, currentY + 1);
			if (top != null && !tilesInList.contains(top)) {
				tilesToFill.add(tile(currentX, currentY + 1, top));
				tilesInList.add(top);
			}

			final MapTileDefinition bottom = map.getTileDefinition(currentX, currentY - 1);
			if (bottom != null && !tilesInList.contains(bottom)) {
				tilesToFill.add(tile(currentX, currentY - 1, bottom));
				tilesInList.add(bottom);
			}
		}
	}

	/** @param zoom the amount to zoom
	 * @return a new map widget, consisting of several {@link MapTileWidget}
	 *         classes to represent the current {@link MapDefinition}. */
	public Actor getActor(final int zoom) {
		if (actor == null) {
			this.zoom = zoom;
			tiles.clear();
			actor = new Stack() {

				{

					add(new Table() {
						{
							for (int j = 0; j < module.getDefinition().getHeight(); j++) {
								Table row = new Table();
								for (int i = 0; i < module.getDefinition().getWidth(); i++) {
									final MapTileWidget widget = new MapTileWidget(i, j, module.getDefinition(), MapEditorWidget.this);
									row.add(widget).size(32 / zoom).pad(0);
									tiles.add(widget);
								}
								add(row).row();
							}
							addListener(new ClickListener() {
								@Override
								public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
									entered = true;
								};

								@Override
								public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
									entered = false;
								};
							});
						}

						@Override
						public void act(float delta) {
							super.act(delta);
							if (MapTileWidget.selX >= 0 && MapTileWidget.selY >= 0 && editMode == EditMode.ALTITUDE) {
								if (Gdx.input.isKeyJustPressed(Keys.F)) {
									if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
										fill(MapTileWidget.selX, MapTileWidget.selY, false);
									} else {
										fill(MapTileWidget.selX, MapTileWidget.selY, true);
									}
									module.getDefinition().resetFilled();
								} else if (Gdx.input.isKeyJustPressed(Keys.LEFT)) {
									module.getDefinition().westX = MapTileWidget.selX;
									module.getDefinition().westY = module.getDefinition().getHeight() - MapTileWidget.selY - 1;
									updateTeleports();
								} else if (Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
									module.getDefinition().eastX = MapTileWidget.selX;
									module.getDefinition().eastY = module.getDefinition().getHeight() - MapTileWidget.selY - 1;
									updateTeleports();
								} else if (Gdx.input.isKeyJustPressed(Keys.UP)) {
									module.getDefinition().northX = MapTileWidget.selX;
									module.getDefinition().northY = module.getDefinition().getHeight() - MapTileWidget.selY - 1;
									updateTeleports();
								} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
									module.getDefinition().southX = MapTileWidget.selX;
									module.getDefinition().southY = module.getDefinition().getHeight() - MapTileWidget.selY - 1;
									updateTeleports();
								}

							}
						}

					});
					add(new Table() {
						{
							add(helpTable = new Table());
							helpTable.setBackground(Drawables.get("black"));
							helpTable.add(new VisLabel("0-9: Set Altitude")).row();
							helpTable.add(new VisLabel("Q,W,E,R,T,S: Set Terrain Type")).row();
							helpTable.add(new VisLabel("A: Set Tree Wall (Q Reverts)")).row();
							helpTable.add(new VisLabel("Z,X,C,V,B,N: Set Upper Terrain Type")).row();
							helpTable.add(new VisLabel("F: Fill | Ctrl + F: Dig ")).row();
							helpTable.add(new VisLabel("Arrows: Set Teleport")).row();
							WidgetUtils.divider(helpTable, "yellow");
							helpTable.add(coordinates = new VisLabel()).row();
							helpTable.setTouchable(Touchable.disabled);
						}

						@Override
						public void act(float delta) {
							super.act(delta);
							helpTable.setVisible(Gdx.input.isKeyPressed(Keys.F2));
							//coordinates.setText(String.format("(%d, %d)", MapTileWidget.selX, module.getDefinition().getHeight() - MapTileWidget.selY));
							helpTable.setPosition(Gdx.input.getX() - 600, Gdx.graphics.getHeight() - Gdx.input.getY());
						}

					});
					add(new Table() {
						{
							add(popupTable = new Table()).size(300);
						}
					});
				}

				@Override
				public void draw(Batch batch, float parentAlpha) {
					super.draw(batch, parentAlpha);
					for (int i = 0; i < tiles.size; i++) {
						tiles.get(i).drawEntity(batch);
					}
				}

			};
		}
		return actor;
	}

	/** @return the current {@link EditMode} */
	public EditMode getEditMode() {
		return editMode;
	}

	public int getZoom() {
		return zoom;
	}

	/** @return <code>true</code> if the mouse has entered the editor widget. */
	public boolean isEntered() {
		return entered;
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
							ObjectMap<String, EntityDefinition> entities = new Json().fromJson(ListDefinition.class, handle).items();
							for (EntityDefinition entity : entities.values()) {
								add(entity.name);
							}
						}
					}
				});
				cols = 1;
			}
		};
	}

	/** Sets the edit mode, which dictates the action to take when a tile is
	 * clicked.
	 * @param editMode the {@link EditMode} */
	public void setEditMode(EditMode editMode) {
		MapEditorWidget.editMode = editMode;
	}

	/** Sets a listener to be invoked when a teleport tile is placed
	 * @param listener the {@link Listener} */
	public void setTeleportChangeListener(Listener listener) {
		this.listener = listener;
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
		final Table buttonTable = new Table();
		buttonTable.add(WidgetUtils.button("Close", closeListener(entity)));
		buttonTable.add(WidgetUtils.button("Delete", deleteListener(entity)));
		popupTable.add(buttonTable).expandX().fill();
	}

	private void updateTeleports() {
		if (listener != null) {
			listener.invoke();
		}
	}
}
