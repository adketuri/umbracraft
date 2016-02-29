package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent.Direction;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** A component to handle map collision.
 * @author Andrew Keturi */
public class MapCollisionComponent extends BaseComponent {

	private BitmapFont debug;
	private final int height, width;
	private boolean isControlled;
	private final Map map;
	private boolean onGround, onStairs;

	public MapCollisionComponent(int width, int height) {
		this.width = width;
		this.height = height;
		map = Game.map();
	}

	private void checkJump(Direction direction, Entity entity) {
		if (onStairs || !isControlled) {
			return;
		}
		final float len = Math.abs(entity.velocity.x) + Math.abs(entity.velocity.y);
		if (len < 1.5f) {
			return;
		}
		// get the tile coordinates of the entity
		float nearX = (entity.position.x);
		float farX = (entity.position.x);
		float nearY = (entity.position.y);
		float farY = (entity.position.y);
		float z = entity.position.z / Config.tileWidth;
		switch (direction) {
		case UP:
			farY += 14;
			break;
		case DOWN:
			farY -= 14;
			break;
		case LEFT:
			farX -= 14;
			break;
		case RIGHT:
			farX += 14;
			break;
		default:
			break;
		}
		nearX /= Config.tileWidth;
		nearY /= Config.tileWidth;
		farX /= Config.tileWidth;
		farY /= Config.tileWidth;

		// check for jumping from a higher to lower altitude
		if (map.getAltitudeAt(nearX, nearY) < (int) z && onGround && !onStairs) {
			onGround = false;
			entity.velocity.z = 5;
		}

		// check if the altitude in front of the player is just one tile up
		if ((map.getAltitudeAt(farX, farY) - 1 == z) && onGround && !onStairs) {
			onGround = false;
			entity.velocity.z = 5;
		}
	}

	@Override
	public void create(Entity entity) {
		debug = Game.assets().get("fonts/message.fnt", BitmapFont.class);
		isControlled = entity.getComponent(ControlledInputComponent.class) != null;
	}

	@Override
	public void dispose(Entity entity) {

	}

	/** @return the component's height */
	public int getHeight() {
		return height;
	}

	/** @return the component's width */
	public int getWidth() {
		return width;
	}

	/** @return whether or not the entity is colliding with the ground */
	public boolean isOnGround() {
		return onGround;
	}

	/** @return whether or not the entity is on stairs */
	public boolean isOnStairs() {
		return onStairs;
	}

	@Override
	public void render(Entity entity) {
		int tileX1 = (int) (entity.position.x + width) / Config.tileWidth;
		int tileY = (int) (entity.position.y + height + entity.velocity.y) / Config.tileWidth;
		if (Game.isDebug()) {
			debug.draw(Game.batch(), map.getAltitudeAt(tileX1, tileY) + "", width, height);
			Game.batch().draw(Game.assets().get("debug.png", Texture.class), entity.position.x - width / 2, entity.position.y - height / 2, width, height);
		}
	}

	@Override
	public void update(Entity entity) {

		// stair updates
		int centerX = (int) (entity.position.x) / Config.tileWidth;
		int centerY = (int) (entity.position.y) / Config.tileWidth;
		int tileYNorth = (int) (2 + entity.position.y + height / 2) / Config.tileWidth;
		int tileYSouth = (int) ((entity.position.y - height / 2) - 0) / Config.tileWidth;

		final float tileAltitudeFloat = (entity.position.z / Config.tileWidth);
		if (map.getTypeAt(centerX, centerY) == map.getDefinition().stairs) {
			if (tileAltitudeFloat < map.getAltitudeAt(centerX, tileYNorth) && entity.velocity.y > 0) {
				entity.velocity.z = entity.velocity.y;
				entity.velocity.y = 0;
			} else if (tileAltitudeFloat > map.getAltitudeAt(centerX, tileYSouth) && entity.velocity.y < 0) {
				entity.velocity.z = entity.velocity.y;
				entity.velocity.y = 0;
			}
			onStairs = true;
		} else {
			onStairs = false;
		}
		final int tileAltitude = (int) (entity.position.z / Config.tileWidth);

		// update position
		// check for collisions
		if (entity.velocity.y > 0) {
			// NORTH
			int tileX1 = (int) (entity.position.x + width / 2) / Config.tileWidth;
			int tileX2 = (int) (entity.position.x - width / 2) / Config.tileWidth;
			int tileY = (int) (entity.position.y + height / 2 + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				entity.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x -= 2f;
				}
			}
			checkJump(Direction.UP, entity);
		} else if (entity.velocity.y < 0 && !onStairs) {
			// SOUTH
			int tileX1 = (int) (entity.position.x + width / 2) / Config.tileWidth;
			int tileX2 = (int) (entity.position.x - width / 2) / Config.tileWidth;
			int tileY = (int) (entity.position.y - height / 2 + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX1, tileY) > tileAltitude || map.getAltitudeAt(tileX2, tileY) > tileAltitude) {
				entity.velocity.y = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX1, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x += 2f;
				} else if (map.getAltitudeAt(tileX2, tileY) <= tileAltitude && entity.velocity.x == 0) {
					entity.velocity.x -= 2f;
				}
			}
			checkJump(Direction.DOWN, entity);
		}
		if (entity.velocity.x > 0) {
			// RIGHT
			int tileX = (int) (entity.position.x + width / 2 + entity.velocity.x) / Config.tileWidth;
			int tileY1 = (int) (entity.position.y + height / 2 + entity.velocity.y) / Config.tileWidth;
			int tileY2 = (int) (entity.position.y - height / 2 + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				entity.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y -= 2f;
				}
			}
			checkJump(Direction.RIGHT, entity);
		} else if (entity.velocity.x < 0) {
			// LEFT
			int tileX = (int) (entity.position.x - width / 2 + entity.velocity.x) / Config.tileWidth;
			int tileY2 = (int) (entity.position.y - height / 2 + entity.velocity.y) / Config.tileWidth;
			int tileY1 = (int) (entity.position.y + height / 2 + entity.velocity.y) / Config.tileWidth;
			if (map.getAltitudeAt(tileX, tileY1) > tileAltitude || map.getAltitudeAt(tileX, tileY2) > tileAltitude) {
				entity.velocity.x = 0;
				// nudge out if we're stuck
				if (map.getAltitudeAt(tileX, tileY1) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y += 2f;
				} else if (map.getAltitudeAt(tileX, tileY2) <= tileAltitude && entity.velocity.y == 0) {
					entity.velocity.y -= 2f;
				}
			}
			checkJump(Direction.LEFT, entity);
		}
		entity.position.add(entity.velocity);

		// check for stairs, falling, or placement
		if (onStairs) {
			// reset back to y so the animations work correctly
			entity.velocity.y = entity.velocity.z;
			entity.velocity.z = 0;
		} else if (entity.position.z / Config.tileWidth > map.getAltitudeAt(centerX, centerY)) {
			onGround = false;
			entity.velocity.z -= 0.5f;
		} else {
			onGround = true;
			entity.velocity.z = 0;
			entity.position.z = map.getAltitudeAt(centerX, centerY) * Config.tileWidth;
		}

		// check if we're STILL inside a wall, and if so gently nudge out
		if (onGround && !onStairs && entity.velocity.z <= 0) {
			// north/south
			int tileY1 = (int) (entity.position.y + height / 2) / Config.tileWidth;
			int tileY2 = (int) (entity.position.y - height / 2) / Config.tileWidth;
			if (map.getAltitudeAt(centerX, tileY1) > map.getAltitudeAt(centerX, centerY)) {
				entity.position.y -= 0.75f;
			} else if (map.getAltitudeAt(centerX, tileY2) > map.getAltitudeAt(centerX, centerY)) {
				entity.position.y += 0.75f;
			}
			// east/west
			int tileX1 = (int) (entity.position.x + width / 2) / Config.tileWidth;
			int tileX2 = (int) (entity.position.x - width / 2) / Config.tileWidth;
			if (map.getAltitudeAt(tileX1, centerY) > map.getAltitudeAt(centerX, centerY)) {
				entity.position.x -= 1f;
			} else if (map.getAltitudeAt(tileX2, centerY) > map.getAltitudeAt(centerX, centerY)) {
				entity.position.x += 1f;
			}
		}

	}
}
