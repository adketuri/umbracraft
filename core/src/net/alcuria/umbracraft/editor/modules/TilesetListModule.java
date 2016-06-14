package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class TilesetListModule extends ListModule<TilesetDefinition> {

	@Override
	public void addListItem() {
		final TilesetDefinition item = new TilesetDefinition();
		item.name = "Tileset " + rootDefinition.size();
		rootDefinition.add(item);
	}

	@Override
	public void create(TilesetDefinition definition, Table content) {
		final PopulateConfig config = new PopulateConfig();
		config.cols = 5;
		config.textFieldWidth = 100;
		content.add(getTilesetPreview(definition)).row();
		populate(content, TilesetDefinition.class, definition, config);
	}

	private Actor getTilesetPreview(TilesetDefinition definition) {
		if (definition != null && definition.filename != null && Gdx.files.internal("tiles/" + definition.filename).exists()) {
			Stack stack = new Stack();
			stack.add(new Image(new Texture(Gdx.files.internal("tiles/" + definition.filename))));
			stack.add(new Image(new Texture(Gdx.files.internal("engine/numbers.png"))));
			return stack;
		}
		return new Table() {
			{
				add().expand().fill().size(256);
			}
		};
	}

	@Override
	public String getTitle() {
		return "Tilesets";
	}

}
