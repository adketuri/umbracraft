package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MapTileWidget extends Table {

	private static TextureRegion side, top;
	private int altitude = 0;
	private final MapDefinition definition;
	private final int i, j;

	public MapTileWidget(int x, int y, final MapDefinition definition) {
		i = x;
		j = y;
		this.definition = definition;
		if (side == null) {
			Texture skin = new Texture(Gdx.files.internal("editor/skin.png"));
			side = new TextureRegion(skin, 4, 0, 1, 1);
			top = new TextureRegion(skin, 3, 0, 1, 1);
		}
		setBackground(Drawables.get("blue"));
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				altitude++;
				MapTileWidget.this.definition.tiles[i][j].altitude++;
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				setBackground(Drawables.get("yellow"));
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				setBackground(Drawables.get("blue"));
			}
		});
		setTouchable(Touchable.enabled);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(side, getX(), getY(), getWidth(), getWidth() * altitude);
		batch.draw(top, getX(), getY() + altitude * getHeight(), getWidth(), getHeight());
	}
}
