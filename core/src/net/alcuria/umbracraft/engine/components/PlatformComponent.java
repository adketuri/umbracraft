package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class PlatformComponent implements Component {

	private final Rectangle bounds = new Rectangle(0, 0, Config.tileWidth + 2, Config.tileWidth + 2);

	private void checkCollision(final Entity platform, final Entity otherEntity) {
		MapCollisionComponent otherCollision = otherEntity.getComponent(MapCollisionComponent.class);
		if (otherCollision != null) {
			float otherX = otherEntity.position.x;
			float otherY = otherEntity.position.y;
			if (bounds.contains(otherX, otherY) && MathUtils.isEqual(otherEntity.position.z, platform.position.z, 2)) {
				otherCollision.setOnPlatform();
				otherEntity.position.add(platform.velocity);
				otherEntity.position.z = platform.position.z;
				otherEntity.velocity.z = 0;
				Game.log("ON");
			}
		}
	}

	@Override
	public void create(Entity entity) {

		int centerX = (int) (entity.position.x) / Config.tileWidth;
		int centerY = (int) (entity.position.y) / Config.tileWidth;
		int altitude = Game.map().getAltitudeAt(centerX, centerY);
		if (entity.getArguments().size > 0) {
			if (StringUtils.isNumber(entity.getArguments().get(0))) {
				altitude += Integer.valueOf(entity.getArguments().get(0));
			}
		}
		entity.position.z = altitude * Config.tileWidth;
		entity.position.x += Config.tileWidth / 2;
		entity.position.y += Config.tileWidth / 2; // TODO: should this be origin?
	}

	@Override
	public void dispose(Entity entity) {

	}

	@Override
	public void render(Entity entity) {
		if (Game.isDebug()) {
			Game.batch().draw(Game.assets().get("debug.png", Texture.class), bounds.x, bounds.y + entity.position.z, bounds.width, bounds.height);
		}
	}

	@Override
	public void update(Entity entity) {
		entity.position.add(entity.velocity);
		bounds.setCenter(entity.position.x, entity.position.y);
		for (EntityScope scope : EntityScope.values()) {
			for (final Entity otherEntity : Game.entities().getEntities(scope)) {
				checkCollision(entity, otherEntity);
			}
		}
	}
}
