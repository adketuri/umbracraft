package net.alcuria.umbracraft.layouts;

import net.alcuria.umbracraft.modules.Module;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class MapLayout extends Layout {
	private Stage stage;
	private Table content;

	public MapLayout() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		Table root = new Table();
		content = new Table();
		root.setFillParent(true);
		root.add(new Table(){
			{
				add(content).expand().fill();
			}
		}).expand().fill();
		stage.addActor(root);
	}
	
	@Override
	public void update(float delta) {
		stage.act();		
	}

	@Override
	public void render() {
		stage.draw();		
	}

}
