package net.alcuria.umbracraft.engine.screens;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.map.TeleportDefinition.TeleportDirection;
import net.alcuria.umbracraft.engine.AreaBuilder;
import net.alcuria.umbracraft.engine.components.MapCollisionComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

/** The teleporter is responsible for determining when the player entity reaches
 * the edge of a map, finding the appropriate map to teleport her to, and
 * notifying the {@link WorldScreen} that we want to change maps.
 * @author Andrew Keturi */
public class Teleporter {

	private enum TeleportState {
		NOT_STARTED, STARTED, TRANSITIONED
	}

	private static final float FADE_TIME = 0.5f;
	private AreaBuilder areaBuilder;
	private TeleportDirection direction;
	private Entity player;
	private int playerWidth, playerHeight;
	private TeleportState state = TeleportState.NOT_STARTED;

	private float time;

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
		state = TeleportState.NOT_STARTED;
		time = 0;
		Game.publisher().publish(new SetInputEnabled(true));
	}

	/** Frees up any used assets. */
	public void dispose() {
		player = null;
	}

	private void startTeleport(TeleportDirection direction) {
		state = TeleportState.STARTED;
		time = 0;
		this.direction = direction;
		Game.publisher().publish(new SetInputEnabled(false));
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
			if (state == TeleportState.NOT_STARTED) {
				checkEdges();
			} else {
				updateTeleport();
			}
		}
	}

	private void updateTeleport() {
		if (time < FADE_TIME) {
			// fade out
			final float color = (1 - time / FADE_TIME) * (1 - time / FADE_TIME);
			Game.batch().setColor(new Color(color, color, color, 1));
			time += Gdx.graphics.getDeltaTime();
		} else {
			// fade in
			final float color = ((time - FADE_TIME) / FADE_TIME) * ((time - FADE_TIME) / FADE_TIME);
			Game.batch().setColor(new Color(color, color, color, 1));
			time += Gdx.graphics.getDeltaTime();
			if (state != TeleportState.TRANSITIONED) {
				state = TeleportState.TRANSITIONED;
				time = FADE_TIME;
				Game.areas().changeNode(direction);
				Game.view().setBounds(new Rectangle(0, 0, Game.map().getWidth() * Config.tileWidth, Game.map().getHeight() * Config.tileWidth));
				Game.view().focus();
				player = Game.entities().find(Entity.PLAYER);
			}
			if (time >= 2 * FADE_TIME) {
				completeTeleport();
			}
		}
	}

}
