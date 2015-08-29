package net.alcuria.umbracraft;

import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.editor.layout.EditorLayout;
import net.alcuria.umbracraft.editor.layout.Layout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.ui.VisUI;

/** The editor.
 * @author Andrew Keturi */
public class UmbraCraftEditor implements ApplicationListener {

	private SpriteBatch batch;
	private Editor editor;
	private Layout view;

	@Override
	public void create() {
		VisUI.load();
		Drawables.init();
		editor = new Editor();
		batch = new SpriteBatch();
		view = new EditorLayout();
	}

	@Override
	public void dispose() {
		VisUI.dispose();
		batch.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		view.update(Gdx.graphics.getDeltaTime());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		view.render(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		view.resize(width, height);
	}

	@Override
	public void resume() {
	}
}