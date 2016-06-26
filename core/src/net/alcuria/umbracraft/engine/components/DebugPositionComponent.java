package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

public class DebugPositionComponent implements Component {

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
		Game.batch().draw(debug, entity.position.x, entity.position.y + entity.position.z);
	}

	@Override
	public void update(Entity entity) {
		if (Gdx.input.isKeyPressed(Keys.EQUALS)) {
			entity.position.y++;
		} else if (Gdx.input.isKeyPressed(Keys.MINUS)) {
			entity.position.y--;
		}
	}

}
