package net.alcuria.umbracraft.engine.objects;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.BaseComponent;
import net.alcuria.umbracraft.engine.components.InputComponent;
import net.alcuria.umbracraft.engine.components.PhysicsComponent;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.map.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** The GameObjectCreator is responsible for instantiating various game objects
 * (players, etc) to be used by the {@link GameObjectManager}
 * @author Andrew Keturi */
public class GameObjectCreator {

	/** @param map
	 * @return A player {@link GameObject} */
	public static GameObject player(Map map) {
		GameObject player = new GameObject(new BaseComponent() {
			private TextureRegion character;

			@Override
			public void create() {
				character = new TextureRegion(Game.assets().get("sprites/andoru.png", Texture.class), 16, 27);
			}

			@Override
			public void dispose() {

			}

			@Override
			public void render(GameObject object) {
				Game.batch().draw(character, object.position.x, object.position.y);
			}

			@Override
			public void update(GameObject object) {

			}
		});
		// input
		final InputComponent input = new InputComponent();
		Gdx.input.setInputProcessor(input);
		player.addComponent(input);
		player.addComponent(new PhysicsComponent(map));
		Game.publisher().publish(new CameraTargetEvent(player));
		return player;
	}
}
