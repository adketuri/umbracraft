package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/** A widget to preview a particular map with the ability to click on a tile and
 * view its coordinates.
 * @author Andrew Keturi */
public class MapPreviewWidget {

	public Table root;

	public MapPreviewWidget(MapDefinition definition) {
	}

	public Actor getActor() {
		if (root == null) {
			root = new Table();
			update();
		}
		return root;
	}

	public void update() {
		root.clear();
		root.add(new SuggestionWidget(Editor.db().list(MapDefinition.class).keys()).getActor());
	}

}
