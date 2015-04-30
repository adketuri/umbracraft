package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.map.Map;
import net.alcuria.umbracraft.engine.objects.GameObject;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PhysicsComponent implements BaseComponent {

	private BitmapFont debug;
	private final int height = 8, width = 14; //FIXME: don't hardcode
	private final Map map;

	public PhysicsComponent(Map map) {
		this.map = map;
	}

	@Override
	public void create() {
		debug = Game.assets().get("fonts/message.fnt", BitmapFont.class);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(GameObject object) {
		int tileX1 = (int) (object.position.x + width) / Game.config().tileWidth;
		int tileY = (int) (object.position.y + height + object.velocity.y) / Game.config().tileWidth;
		debug.draw(Game.batch(), map.getAltitudeAt(tileX1, tileY) + "", -20, 10);
	}

	@Override
	public void update(GameObject object) {

		int tileAltitude = object.altitude / Game.config().tileWidth;
		// check for collisions
		if (object.velocity.y > 0) {
			// NORTH
			int tileX1 = (int) (object.position.x + width) / Game.config().tileWidth;
			int tileX2 = (int) (object.position.x) / Game.config().tileWidth;
			int tileY = (int) (object.position.y + height + object.velocity.y) / Game.config().tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				object.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && object.velocity.x == 0) {
					object.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && object.velocity.x == 0) {
					object.velocity.x -= 2f;
				}
			}

		} else if (object.velocity.y < 0) {
			// SOUTH
			int tileX1 = (int) (object.position.x + width) / Game.config().tileWidth;
			int tileX2 = (int) (object.position.x) / Game.config().tileWidth;
			int tileY = (int) (object.position.y + object.velocity.y) / Game.config().tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				object.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && object.velocity.x == 0) {
					object.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && object.velocity.x == 0) {
					object.velocity.x -= 2f;
				}
			}
		}

		if (object.velocity.x > 0) {
			// RIGHT
			int tileX = (int) (object.position.x + width + object.velocity.x) / Game.config().tileWidth;
			int tileY1 = (int) (object.position.y + height + object.velocity.y) / Game.config().tileWidth;
			int tileY2 = (int) (object.position.y + object.velocity.y) / Game.config().tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				object.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && object.velocity.y == 0) {
					object.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && object.velocity.y == 0) {
					object.velocity.y -= 2f;
				}

			}
		} else if (object.velocity.x < 0) {
			// LEFT
			int tileX = (int) (object.position.x + object.velocity.x) / Game.config().tileWidth;
			int tileY2 = (int) (object.position.y + object.velocity.y) / Game.config().tileWidth;
			int tileY1 = (int) (object.position.y + height + object.velocity.y) / Game.config().tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				object.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && object.velocity.y == 0) {
					object.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && object.velocity.y == 0) {
					object.velocity.y -= 2f;
				}
			}
		}
		object.position.add(object.velocity);

	}
}
