package net.alcuria.umbracraft;

import net.alcuria.umbracraft.views.MainView;
import net.alcuria.umbracraft.views.View;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;

public class UmbraCraft extends ApplicationAdapter {
	private SpriteBatch batch;
	View view;
	
	@Override
	public void create () {
		VisUI.load();
		batch = new SpriteBatch();
		view = new MainView();
	}

	@Override
	public void dispose() {
		super.dispose();
		VisUI.dispose();
		batch.dispose();
	}
	
	@Override
	public void render () {
		view.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		view.render();
		batch.end();
	}
}
