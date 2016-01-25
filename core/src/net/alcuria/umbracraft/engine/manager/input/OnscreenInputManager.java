package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.manager.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class OnscreenInputManager extends Manager<OnscreenInput> {

	private final Stage stage;

	public OnscreenInputManager() {
		stage = new Stage(Game.view().getViewport());
		final TouchpadEntity touchpad = new TouchpadEntity(stage);
		Gdx.input.setInputProcessor(new InputMultiplexer() {
			{
				addProcessor(touchpad);
				addProcessor(stage);
			}
		});
		add(touchpad);
		add(new MainMenuButton(stage));
		add(new DebugText(stage));
	}

	@Override
	public void render() {
		super.render();
		stage.draw();
	}

	@Override
	public void update() {
		super.update();
		stage.act(Gdx.graphics.getDeltaTime());
	}
}
