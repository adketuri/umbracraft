package net.alcuria.umbracraft.layouts;

import net.alcuria.umbracraft.mapgen.MapGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapGenLayout extends Layout {
	private final Table content;
	private final MapGenerator gen;
	private final Stage stage;

	public MapGenLayout() {
		gen = new MapGenerator();
		stage = new Stage();
		Gdx.input.setInputProcessor(gen);
		Table root = new Table();
		content = new Table();
		root.setFillParent(true);
		root.add(new Table() {
			{
				add(content).expand().fill();
			}
		}).expand().fill();
		stage.addActor(root);
	}

	@Override
	public void render(SpriteBatch batch) {
		stage.draw();
		gen.draw(batch);
	}

	@Override
	public void update(float delta) {
		stage.act();
	}

}
