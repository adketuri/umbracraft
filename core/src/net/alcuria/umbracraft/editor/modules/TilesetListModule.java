package net.alcuria.umbracraft.editor.modules;

import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.util.FileUtils;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

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
		config.suggestions = new ObjectMap<String, Array<String>>() {
			{
				put("filename", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().tilesetPath));
			}
		};
		content.add(getTilesetPreview(definition)).row();
		populate(content, TilesetDefinition.class, definition, config);
	}

	private Actor getTilesetPreview(TilesetDefinition definition) {
		FileHandle handle = Gdx.files.absolute(Editor.db().config().projectPath + Editor.db().config().tilesetPath + definition.filename + ".png");
		if (definition != null && StringUtils.isNotEmpty(definition.filename) && handle.exists()) {
			Stack stack = new Stack();
			stack.add(new Image(new Texture(handle)));
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
