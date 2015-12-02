package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

/** The teleporter is responsible for determining when the player entity reaches
 * the edge of a map, finding the appropriate map to teleport her to, and
 * notifying the {@link WorldScreen} that we want to change maps.
 * @author Andrew Keturi */
public class Teleporter {

	private AreaBuilder areaBuilder;
	private Entity player;
	private int playerWidth, playerHeight;

	/** Frees up any used assets. */
	public void dispose() {
		player = null;
	}

	private void teleport(TeleportDirection direction) {
		Game.areas().changeNode(direction);
		Game.view().focus();
		player = Game.entities().find(Entity.PLAYER);
	}

	/** Update stuff */
	public void update() {
		if (player == null) {
			player = Game.entities().find(Entity.PLAYER);
			try {
				playerWidth = player.getComponent(MapCollisionComponent.class).getWidth();
				playerHeight = player.getComponent(MapCollisionComponent.class).getWidth();
			} catch (NullPointerException e) {
				Game.error("Could not find MapCollisionComponent in player entity. Defaulting to size 0.");
			}
		}
		if (areaBuilder == null) {
			areaBuilder = Game.areas();
		}
		if (player != null) {
			if (player.position.x < 0) {
				if (Game.areas().hasTeleportAt(TeleportDirection.WEST)) {
					teleport(TeleportDirection.WEST);
				} else {
					player.position.x = 0;
				}
			} else if (player.position.x + playerWidth > Game.map().getWidth() * Config.tileWidth) {
				if (Game.areas().hasTeleportAt(TeleportDirection.EAST)) {
					teleport(TeleportDirection.EAST);
				} else {
					player.position.x = Game.map().getWidth() * Config.tileWidth - playerWidth;
				}
			} else if (player.position.y + playerHeight > Game.map().getHeight() * Config.tileWidth) {
				if (Game.areas().hasTeleportAt(TeleportDirection.NORTH)) {
					teleport(TeleportDirection.NORTH);
				} else {
					player.position.y = Game.map().getHeight() * Config.tileWidth - playerHeight;
				}
			} else if (player.position.y < 0) {
				if (Game.areas().hasTeleportAt(TeleportDirection.SOUTH)) {
					teleport(TeleportDirection.SOUTH);
				} else {
					player.position.y = 0;
				}
			}
		}
	}

}
