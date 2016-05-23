package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.Component;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** A component to display a shadow underneath an entity.
 * @author Andrew Keturi */
public class ShadowComponent implements Component {

	private static final int WIDTH = 16, HEIGHT = 16;
	private boolean added;
	private MapCollisionComponent collision;
	private final boolean ignoreMap;
	private TextureRegion shadow;
	private boolean squareShadow = false;
	private final int xOffset, yOffset;

	public ShadowComponent(boolean squareShadow) {
		this(squareShadow, 0, 0, false);
	}

	public ShadowComponent(boolean squareShadow, int x, int y, boolean ignoreMap) {
		xOffset = x;
		yOffset = y;
		this.ignoreMap = ignoreMap;
		this.squareShadow = squareShadow;
	}

	@Override
	public void create(Entity entity) {
		if (squareShadow) {
			shadow = new TextureRegion(Game.assets().get("sprites/shadow.png", Texture.class), 8, 4, 1, 1);
		} else {
			shadow = new TextureRegion(Game.assets().get("sprites/shadow.png", Texture.class), 16, 6);
		}
	}

	@Override
	public void dispose(Entity entity) {
	}

	private int getHeight() {
		return squareShadow ? 16 : 6;
	}

	private int getWidth() {
		return 16;
	}

	@Override
	public void render(Entity entity) {
		final int altitude = Game.map() == null || ignoreMap ? 0 : Game.map().getAltitudeAt((int) entity.position.x / Config.tileWidth, (int) entity.position.y / Config.tileWidth);
		if (collision != null && collision.isOnGround()) {
			Game.batch().draw(shadow, entity.position.x - WIDTH / 2 + xOffset, entity.position.y - HEIGHT / 2 + yOffset + 2 + entity.position.z, getWidth(), getHeight());
		} else {
			Game.batch().draw(shadow, entity.position.x - WIDTH / 2 + xOffset, entity.position.y - HEIGHT / 2 + yOffset + 2 + Config.tileWidth * altitude, getWidth(), getHeight());
		}
	}

	@Override
	public void update(Entity entity) {
		if (collision == null && !added) {
			collision = entity.getComponent(MapCollisionComponent.class);
		}
		added = true;
	}

}
