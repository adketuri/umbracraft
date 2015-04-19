package net.alcuria.umbracraft.engine.components;

import net.alcuria.umbracraft.App;
import net.alcuria.umbracraft.engine.components.base.BaseComponent;
import net.alcuria.umbracraft.engine.components.base.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/** The GameObjectCreator is responsible for instantiating various game objects
 * (players, etc) to be used by the {@link GameObjectManager}
 * @author Andrew Keturi */
public class GameObjectCreator {

	/** @return A player {@link GameObject} */
	public static GameObject player() {
		GameObject player = new GameObject(new BaseComponent() {
			private TextureRegion character;

			@Override
			public void create() {
				character = new TextureRegion(App.assets().get("sprites/andoru.png", Texture.class), 16, 27);
			}

			@Override
			public void dispose() {

			}

			@Override
			public void render(GameObject object) {
				App.batch().draw(character, object.position.x, object.position.y);
			}

			@Override
			public void update(GameObject object) {

			}
		});
		// input
		final InputComponent component = new InputComponent();
		Gdx.input.setInputProcessor(component);
		player.addComponent(component);
		return player;
	}
}
