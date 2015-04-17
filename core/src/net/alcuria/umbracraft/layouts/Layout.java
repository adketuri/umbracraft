package net.alcuria.umbracraft.layouts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Layout {

	public abstract void render(SpriteBatch batch);

	public abstract void update(float delta);
}
