package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** A component to handle collision
 * @author Andrew Keturi */
public class PhysicsComponent implements BaseComponent {

	private BitmapFont debug;
	private final int height = 8, width = 14; //FIXME: don't hardcode
	private final Map map;

	public PhysicsComponent(Map map) {
		this.map = map;
	}

	private void checkJump(Direction direction, Entity entity) {
		// get the tile coordinates of the entity
		float x = (entity.position.x + width / 2) / 16;
		float y = (entity.position.y + height / 2) / 16;
		float z = entity.position.z / 16;
		switch (direction) {
		case UP:
			y += 0.5f;
			break;
		case DOWN:
			y -= 0.5f;
			break;
		case LEFT:
			x -= 0.6f;
			break;
		case RIGHT:
			x += 0.6f;
			break;
		default:
			break;
		}
		// check if the altitude in front of the player is just one tile up or there is a drop
		if ((map.getAltitudeAt(x, y) - 1 == z || map.getAltitudeAt(x, y) < z) && entity.onGround) {
			entity.onGround = false;
			entity.velocity.z = 5;
		}
	}

	@Override
	public void create() {
		debug = Game.assets().get("fonts/message.fnt", BitmapFont.class);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void render(Entity entity) {
		int tileX1 = (int) (entity.position.x + width) / Config.tileWidth;
		int tileY = (int) (entity.position.y + height + entity.velocity.y) / Config.tileWidth;
		debug.draw(Game.batch(), map.getAltitudeAt(tileX1, tileY) + "", 20, 20);
	}

	@Override
	public void update(Entity entity) {

		int tileAltitude = (int) (entity.position.z / Config.tileWidth);
		// check for collisions
		if (entity.velocity.y > 0) {
			// NORTH
			int tileX1 = (int) (entity.position.x + width) / Config.tileWidth;
			int tileX2 = (int) (entity.position.x) / Config.tileWidth;
			int tileY = (int) (entity.position.y + height + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				entity.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x -= 2f;
				}
				checkJump(Direction.UP, entity);
			}
		} else if (entity.velocity.y < 0) {
			// SOUTH
			int tileX1 = (int) (entity.position.x + width) / Config.tileWidth;
			int tileX2 = (int) (entity.position.x) / Config.tileWidth;
			int tileY = (int) (entity.position.y + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				entity.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x -= 2f;
				}
				checkJump(Direction.DOWN, entity);
			}
		}
		if (entity.velocity.x > 0) {
			// RIGHT
			int tileX = (int) (entity.position.x + width + entity.velocity.x) / Config.tileWidth;
			int tileY1 = (int) (entity.position.y + height + entity.velocity.y) / Config.tileWidth;
			int tileY2 = (int) (entity.position.y + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				entity.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y -= 2f;
				}
				checkJump(Direction.RIGHT, entity);
			}
		} else if (entity.velocity.x < 0) {
			// LEFT
			int tileX = (int) (entity.position.x + entity.velocity.x) / Config.tileWidth;
			int tileY2 = (int) (entity.position.y + entity.velocity.y) / Config.tileWidth;
			int tileY1 = (int) (entity.position.y + height + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				entity.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y -= 2f;
				}
				checkJump(Direction.LEFT, entity);
			}
		}
		// update position
		int tileX = (int) (entity.position.x + 5) / Config.tileWidth;
		int tileY = (int) (entity.position.y + 3) / Config.tileWidth;
		entity.position.add(entity.velocity);
		if (entity.position.z / 16f > map.getAltitudeAt(tileX, tileY)) {
			if (entity.onGround) {
				checkJump(Direction.DOWNLEFT, entity);
			}
			entity.onGround = false;
			entity.velocity.z -= 0.5f;
		} else {
			entity.onGround = true;
			entity.velocity.z = 0;
			entity.position.z = map.getAltitudeAt(tileX, tileY) * Config.tileWidth;
		}
	}
}
