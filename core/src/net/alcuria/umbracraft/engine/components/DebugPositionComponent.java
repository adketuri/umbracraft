package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.graphics.Texture;

public class DebugPositionComponent extends BaseComponent {

	Texture debug;

	@Override
	public void create(Entity entity) {
		debug = Game.assets().get("sprites/debug.png", Texture.class);
	}

	@Override
	public void dispose(Entity entity) {
		debug.dispose();
	}

	@Override
	public void render(Entity entity) {
		Game.batch().draw(debug, entity.position.x, entity.position.y);
	}

	@Override
	public void update(Entity entity) {
	}

}
