package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.entities.Entity;

/** The teleporter is responsible for determining when the player entity reaches
 * the edge of a map, finding the appropriate map to teleport her to, and
 * notifying the {@link World} that we want to change maps.
 * @author Andrew Keturi */
public class Teleporter {

	private AreaBuilder areaBuilder;
	private Entity player;

	/** Frees up any used assets. */
	public void dispose() {
		player = null;
	}

	private void teleport(TeleportDirection direction) {
		Game.areas().changeNode(direction);
	}

	/** Update stuff */
	public void update() {
		if (player == null) {
			player = Game.entities().find(Entity.PLAYER);
		}
		if (areaBuilder == null) {
			areaBuilder = Game.areas();
		}
		if (player.position.x < 0) {
			if (Game.areas().hasTeleportAt(TeleportDirection.WEST)) {
				teleport(TeleportDirection.WEST);
			} else {
				player.position.x = 0;
			}
		}
	}

}
