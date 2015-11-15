package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.Component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** A component to display a shadow underneath an entity.
 * @author Andrew Keturi */
public class ShadowComponent implements Component {

	private static final int WIDTH = 16, HEIGHT = 16;
	private TextureRegion shadow;
	private final int xOffset, yOffset;

	public ShadowComponent() {
		this(0, 0);
	}

	public ShadowComponent(int x, int y) {
		xOffset = x;
		yOffset = y;
	}

	@Override
	public void create(Entity entity) {
		shadow = new TextureRegion(Game.assets().get("sprites/shadow.png", Texture.class), 16, 16);
	}

	@Override
	public void dispose(Entity entity) {
	}

	@Override
	public void render(Entity entity) {
		final int altitude = Game.map() == null ? 0 : Game.map().getAltitudeAt((int) entity.position.x / Config.tileWidth, (int) entity.position.y / Config.tileWidth);
		Game.batch().draw(shadow, entity.position.x - WIDTH / 2 + xOffset, entity.position.y - HEIGHT / 2 + yOffset + Config.tileWidth * altitude);
	}

	@Override
	public void update(Entity entity) {
	}

}
