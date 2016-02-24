package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.TintScreen;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

/** The teleporter is responsible for determining when the player entity reaches
 * the edge of a map, finding the appropriate map to teleport her to, and
 * notifying the {@link WorldScreen} that we want to change maps.
 * @author Andrew Keturi */
public class Teleporter implements Disposable {

	private static final float FADE_TIME = 0.5f;
	private AreaBuilder areaBuilder;
	private Entity player;
	private int playerWidth, playerHeight;
	private boolean teleporting;

	private void checkEdges() {
		if (player.position.x - playerWidth / 2 < 0) {
			if (Game.areas().hasTeleportAt(TeleportDirection.WEST)) {
				startTeleport(TeleportDirection.WEST);
			} else {
				player.position.x = playerWidth / 2;
			}
		} else if (player.position.x + playerWidth / 2 > Game.map().getWidth() * Config.tileWidth) {
			if (Game.areas().hasTeleportAt(TeleportDirection.EAST)) {
				startTeleport(TeleportDirection.EAST);
			} else {
				player.position.x = Game.map().getWidth() * Config.tileWidth - playerWidth / 2;
			}
		}
		if (player.position.y + playerHeight / 2 > Game.map().getHeight() * Config.tileWidth) {
			if (Game.areas().hasTeleportAt(TeleportDirection.NORTH)) {
				startTeleport(TeleportDirection.NORTH);
			} else {
				player.position.y = Game.map().getHeight() * Config.tileWidth - playerHeight / 2;
			}
		} else if (player.position.y - playerHeight / 2 < 0) {
			if (Game.areas().hasTeleportAt(TeleportDirection.SOUTH)) {
				startTeleport(TeleportDirection.SOUTH);
			} else {
				player.position.y = playerHeight / 2;
			}
		}
	}

	private void completeTeleport() {
		Game.publisher().publish(new SetInputEnabled(true));
		teleporting = false;
	}

	/** Frees up any used assets. */
	@Override
	public void dispose() {
		player = null;
	}

	private void startTeleport(final TeleportDirection direction) {
		teleporting = true;
		final Entity player = Game.entities().find(Entity.PLAYER);
		if (player != null) {
			player.velocity.x = 0;
			player.velocity.y = 0;
		}
		Game.publisher().publish(new SetInputEnabled(false));
		Game.publisher().publish(new TintScreen(1, FADE_TIME, new Listener() {

			@Override
			public void invoke() {
				Game.areas().changeNode(direction);
				Game.view().setBounds(new Rectangle(0, 0, Game.map().getWidth() * Config.tileWidth, Game.map().getHeight() * Config.tileWidth));
				Game.view().focus();
				Game.publisher().publish(new TintScreen(0, FADE_TIME, new Listener() {

					@Override
					public void invoke() {
						completeTeleport();
					}
				}));
			}
		}));
	}

	/** Update stuff */
	public void update() {
		if (player == null) {
			player = Game.entities().find(Entity.PLAYER);
			if (player != null) {
				playerWidth = player.getComponent(MapCollisionComponent.class).getWidth();
				playerHeight = player.getComponent(MapCollisionComponent.class).getWidth();
			}
		}
		if (areaBuilder == null) {
			areaBuilder = Game.areas();
		}
		if (player != null) {
			if (!teleporting) {
				checkEdges();
			}
		}
	}

}
