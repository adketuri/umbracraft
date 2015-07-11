package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.BaseComponent;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** A component to display a shadow underneath an entity.
 * @author Andrew Keturi */
public class ShadowComponent implements BaseComponent {

	private final Map map;
	private TextureRegion shadow;

	public ShadowComponent(Map map) {
		this.map = map;
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
		Game.batch().draw(shadow, entity.position.x, entity.position.y - 6 + Config.tileWidth * map.getAltitudeAt((int) entity.position.x / Config.tileWidth, (int) entity.position.y / Config.tileWidth));
	}

	@Override
	public void update(Entity entity) {
	}

}