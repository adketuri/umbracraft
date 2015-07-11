package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.EntityCollisionComponent;
import net.alcuria.umbracraft.engine.components.InputComponent;
import net.alcuria.umbracraft.engine.components.PhysicsComponent;
import net.alcuria.umbracraft.engine.components.ScriptComponent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/** The EntityCreator is responsible for instantiating various game objects
 * (players, etc) to be used by the {@link EntityManager}. TODO: perhaps read
 * all these entity definitions from a file.
 * @author Andrew Keturi */
public final class EntityCreator {

	public static Entity dummy(final Map map) {
		final Entity entity = new Entity(new AnimationComponent(Game.db().anim("Andoru-Down-Walking")));
		entity.position.x = MathUtils.random(0, map.getWidth() * 16);
		entity.position.y = MathUtils.random(0, map.getHeight() * 16);
		entity.position.z = 5;
		entity.addComponent(new PhysicsComponent(map, 16, 8));
		return entity;
	}

	public static Entity event(Map map) {
		Entity event = new Entity();
		event.setName("Chest");
		event.addComponent(new AnimationComponent(Game.db().anim("Chest")));
		event.addComponent(new ScriptComponent());
		event.position.x = MathUtils.random(0, 3 * 16);
		event.position.y = MathUtils.random(0, 1 * 16);
		event.addComponent(new PhysicsComponent(map, 16, 8));
		return event;
	}

	/** @param map
	 * @return A player {@link Entity} */
	public static Entity player(final Map map) {
		Entity player = new Entity();
		final InputComponent input = new InputComponent();
		Gdx.input.setInputProcessor(input);
		player.addComponent(input);
		player.addComponent(new PhysicsComponent(map, 16, 8));
		player.addComponent(new EntityCollisionComponent());
		player.addComponent(new ShadowComponent(map));
		player.addComponent(new AnimationCollectionComponent(Game.db().animCollection("Andoru")));
		Game.publisher().publish(new CameraTargetEvent(player));
		player.setName(Entity.PLAYER);
		return player;
	}

	private EntityCreator() {
	}
}
