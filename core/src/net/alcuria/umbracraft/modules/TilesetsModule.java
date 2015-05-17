package net.alcuria.umbracraft.modules;

import net.alcuria.umbracraft.definitions.tileset.TilesetDefinition;
import net.alcuria.umbracraft.definitions.tileset.TilesetListDefinition;
import net.alcuria.umbracraft.widget.WidgetUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** The module for tileset management.
 * @author Andrew Keturi */
public class TilesetsModule extends Module<TilesetListDefinition> {

	public TilesetsModule() {
		super();
		load(TilesetListDefinition.class);
		if (rootDefinition == null) {
			rootDefinition = new TilesetListDefinition();
		}
	}

	private Actor addButton(final Table content) {
		final VisTextButton addButton = new VisTextButton("Add");
		addButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (rootDefinition != null && rootDefinition.tiles != null) {
					rootDefinition.tiles.add(new TilesetDefinition());
					content.clear();
					populate(content);
				}
			}
		});
		return addButton;
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

	@Override
	public void populate(final Table content) {

		content.add(new Table() {
			{
				add(addButton(content));
				add(removeButton(content));
			}
		}).row();
		content.add(new Table() {
			{
				if (rootDefinition.tiles == null) {
					add();
					rootDefinition.tiles = new Array<TilesetDefinition>();
					rootDefinition.tiles.add(new TilesetDefinition());
					populate(this, TilesetDefinition.class, rootDefinition.tiles.get(0), new PopulateConfig());
				} else {
					for (int i = 0; i < rootDefinition.tiles.size; i++) {
						final int index = i;
						add(new Table() {
							{
								add(getTilesetPreview(rootDefinition.tiles.get(index)));
								populate(this, TilesetDefinition.class, rootDefinition.tiles.get(index), new PopulateConfig());
							}
						}).row();
						WidgetUtils.divider(this, "yellow");
					}
				}
			}
		}).row();
		content.add().expand().fill();

	}

	private Actor removeButton(final Table content) {
		final VisTextButton addButton = new VisTextButton("Remove");
		addButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (rootDefinition != null && rootDefinition.tiles != null) {
					rootDefinition.tiles.removeIndex(rootDefinition.tiles.size - 1);
					content.clear();
					populate(content);
				}
			}
		});
		return addButton;
	}
}
