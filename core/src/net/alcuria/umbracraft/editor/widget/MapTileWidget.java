package net.alcuria.umbracraft.editor.widget;

import net.alcuria.umbracraft.definitions.map.MapDefinition;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MapTileWidget extends Table {

	private static TextureRegion side, top, edge, outline;
	private final MapDefinition definition;
	private final int i, j;

	//TODO: this still references an old mapDefinition after updating the size
	public MapTileWidget(int x, int y, final MapDefinition definition) {
		i = x;
		j = y;
		this.definition = definition;
		if (side == null) {
			Texture skin = new Texture(Gdx.files.internal("editor/skin.png"));
			side = new TextureRegion(skin, 4, 0, 1, 1);
			outline = new TextureRegion(skin, 5, 0, 1, 1);
			edge = new TextureRegion(skin, 2, 0, 1, 1);
			top = new TextureRegion(skin, 3, 0, 1, 1);
		}
		setBackground(Drawables.get("blue"));
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
					MapTileWidget.this.definition.tiles.get(i).get(j).altitude--;
				} else {
					MapTileWidget.this.definition.tiles.get(i).get(j).altitude++;
				}
				MapTileWidget.this.definition.tiles.get(i).get(j).altitude = MathUtils.clamp(MapTileWidget.this.definition.tiles.get(i).get(j).altitude, 0, 10);
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

	private int alt(int i, int j) {
		if (i < 0 || i >= definition.tiles.size || j < 0 || j >= definition.tiles.get(0).size) {
			return 0;
		}
		return definition.tiles.get(i).get(j).altitude;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		int altitude = definition.tiles.get(i).get(j).altitude;
		// side
		batch.draw(side, getX(), getY(), getWidth(), getWidth() * altitude);
		// top
		batch.draw(outline, getX(), getY() + altitude * getHeight(), getWidth(), getHeight());
		batch.draw(top, getX() + 1, getY() + altitude * getHeight() + 1, getWidth() - 2, getHeight() - 2);
		// left edge
		if (alt(i - 1, j) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + altitude * getHeight(), 2, getHeight());
		}
		// right edge
		if (alt(i + 1, j) < alt(i, j)) {
			batch.draw(edge, getX() + getWidth() - 2, getY() + altitude * getHeight(), 2, getHeight());
		}
		// bottom edge
		if (alt(i, j + 1) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + altitude * getHeight(), getWidth(), 2);
		}
		// top edge
		if (alt(i, j - 1) < alt(i, j)) {
			batch.draw(edge, getX(), getY() + getHeight() + altitude * getHeight() - 2, getWidth(), 2);
		}
	}
}
