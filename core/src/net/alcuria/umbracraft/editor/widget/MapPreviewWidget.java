package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.Db.DefinitionReference;
import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.definitions.map.MapTileDefinition;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		SuggestionWidget suggestions = new SuggestionWidget(Editor.db().list(MapDefinition.class, DefinitionReference.MAPS).keys(), 200, true);
		root.add(suggestions.getActor()).row();
		suggestions.addSelectListener(updateMapListener(suggestions.getTextField()));
		suggestions.setGenericPopulate(updateMapListener(suggestions.getTextField()));
		root.add(mapTable = new Table()).row();
		root.add(coords = new VisLabel());
	}

	private void updateMap() {
		mapTable.clear();
		if (definition != null) {
			for (int j = 0; j < definition.getHeight(); j++) {
				Table row = new Table();
				for (int i = 0; i < definition.getWidth(); i++) {
					final MapTileDefinition tileDefinition = definition.getTileDefinition(i, j);
					Actor tile = new Image(Drawables.get("white"));
					float color = MathUtils.clamp(1 - (float) 1 / (tileDefinition.altitude + 1), 0, 1);
					tile.setColor(color, color, color, 1);
					row.add(tile).size(4).pad(0);
				}
				mapTable.add(row).row();
			}
		}
	}

	private Listener updateMapListener(final VisTextField textField) {
		return new Listener() {

			@Override
			public void invoke() {
				final MapDefinition current = Editor.db().map(textField.getMessageText());
				if (current != null) {
					definition = current;
					updateMap();
				}
			}
		};
	}

}
