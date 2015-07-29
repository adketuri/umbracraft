package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.map.MapDefinition;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapEditorWidget {

	private final MapDefinition definition;

	public MapEditorWidget(MapDefinition definition) {
		this.definition = definition;
	}

	public Actor getActor() {
		return new Table() {
			{
				for (int j = 0; j < definition.height; j++) {
					Table row = new Table();
					for (int i = 0; i < definition.width; i++) {
						row.add(new MapTileWidget(i, j, definition)).size(32).pad(0);
					}
					add(row).row();
				}
			}
		};
	}

}
