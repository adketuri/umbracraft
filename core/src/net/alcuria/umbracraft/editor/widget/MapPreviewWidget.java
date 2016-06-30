package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Db.DefinitionReference;
import net.alcuria.umbracraft.definitions.map.EntityReferenceDefinition;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.map.MapTileDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;

/** A widget to preview a particular map with the ability to click on a tile and
 * view its coordinates.
 * @author Andrew Keturi */
public class MapPreviewWidget {

	/** Displays selected coordinates */
	private Label coords;
	/** The currently viewed definition. We probably shouldn't modify this */
	private MapDefinition definition;
	/** The color of the last selected actor */
	private Color lastColor;
	/** The last actor the user selected */
	private Actor lastSelected;
	/** Content tables */
	private Table root, mapTable;

	public MapPreviewWidget(MapDefinition definition) {
		this.definition = definition;
	}

	public Actor getActor() {
		if (root == null) {
			root = new Table();
			update();
			updateMap();
		}
		return root;
	}

	public void update() {
		root.clear();
		WidgetUtils.divider(root, "blue");
		root.add(new Table() {
			{
				add(WidgetUtils.tooltip("Enter the name of a map to get a top-down preview. Click to get coordinates."));
				add(new VisLabel("Map Preview:")).padRight(10);
				SuggestionWidget suggestions = new SuggestionWidget(Editor.db().list(MapDefinition.class, DefinitionReference.MAPS).keys(), 200, true);
				add(suggestions.getActor()).row();
				suggestions.addSelectListener(updateMapListener(suggestions.getTextField()));
				suggestions.setGenericPopulate(updateMapListener(suggestions.getTextField()));
			}
		}).row();
		root.add(mapTable = new Table()).row();
		root.add(coords = new VisLabel());
	}

	private void updateMap() {
		mapTable.clear();
		if (definition != null) {
			for (int j = 0; j < definition.getHeight(); j++) {
				Table row = new Table();
				for (int i = 0; i < definition.getWidth(); i++) {

					// check if there is an EntityReference here
					boolean foundEntity = false;
					for (EntityReferenceDefinition entity : definition.entities) {
						if (entity.x == i && entity.y == definition.getHeight() - j - 1) {
							foundEntity = true;
							break;
						}
					}

					// create and add the tile
					final Actor tile = new Image(Drawables.get("white"));
					if (foundEntity) {
						// color this tile red since we have an entity here
						tile.setColor(Color.YELLOW);
						tile.addAction(Actions.forever(Actions.sequence(Actions.alpha(0, 0.5f, Interpolation.sine), Actions.alpha(1, 0.5f, Interpolation.sine))));
					} else {
						// no entity here, adjust the color based on the height
						final MapTileDefinition tileDefinition = definition.getTileDefinition(i, j);
						float color = MathUtils.clamp(1 - tileDefinition.altitude * 0.1f, 0, 1);
						tile.setColor(color, color, color, 1);
					}
					row.add(tile).size(4).pad(0);

					// add a listener that when clicked will display the coordinates
					final int tileX = i;
					final int tileY = definition.getHeight() - j - 1;
					tile.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							coords.setText(String.format("(%d, %d)", tileX, tileY));
							// update the previously-selected tile's color
							if (lastSelected != null && lastColor != null) {
								lastSelected.setColor(lastColor);
							}
							// keep around a reference to this selection for next click, then set the color
							lastSelected = tile;
							lastColor = tile.getColor().cpy();
							tile.setColor(Color.LIME);
						}
					});
				}
				mapTable.add(row).row();
			}
		}
	}

	private Listener updateMapListener(final VisTextField textField) {
		return new Listener() {

			@Override
			public void invoke() {
				// TODO: null checks
				try {
					final MapDefinition current = Editor.db().map(textField.getText());
					if (current != null) {
						definition = current;
						updateMap();
					}
				} catch (NullPointerException e) {
					// couldn't find the map, no worries -- clear any old maps out
					mapTable.clear();
				}
			}
		};
	}

}
