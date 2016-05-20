package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.entities.EntityManager.EntityScope;
import net.alcuria.umbracraft.util.StringUtils;

public class PlatformComponent implements Component {

	private void checkCollision(final Entity entity, final Entity otherEntity) {
		MapCollisionComponent otherCollision = otherEntity.getComponent(MapCollisionComponent.class);
		if (otherCollision != null) {
			int x = (int) (entity.position.x / Config.tileWidth);
			int y = (int) (entity.position.y / Config.tileWidth);
			int otherX = (int) (otherEntity.position.x / Config.tileWidth);
			int otherY = (int) (otherEntity.position.y / Config.tileWidth);
			if (x == otherX && y == otherY) {
				otherCollision.setOnGround(true);
				otherEntity.velocity.z = 0;
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

	}

	@Override
	public void update(Entity entity) {
		for (EntityScope scope : EntityScope.values()) {
			for (final Entity otherEntity : Game.entities().getEntities(scope)) {
				checkCollision(entity, otherEntity);
			}
		}

	}

}
