package net.alcuria.umbracraft.engine.entities;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.InputComponent;
import net.alcuria.umbracraft.engine.components.PhysicsComponent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.Gdx;

/** The EntityCreator is responsible for instantiating various game objects
 * (players, etc) to be used by the {@link EntityManager}. TODO: perhaps read
 * all these entity definitions from a file.
 * @author Andrew Keturi */
public final class EntityCreator {

	/** @param map
	 * @return A player {@link Entity} */
	public static Entity player(final Map map) {
		Entity player = new Entity();
		final InputComponent input = new InputComponent();
		Gdx.input.setInputProcessor(input);
		player.addComponent(input);
		player.addComponent(new PhysicsComponent(map));
		player.addComponent(new AnimationComponent(Game.db().anim("Spin")));
		Game.publisher().publish(new CameraTargetEvent(player));
		return player;
	}

	private EntityCreator() {
	}
}
