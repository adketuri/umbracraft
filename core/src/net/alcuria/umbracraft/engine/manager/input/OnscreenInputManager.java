package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.manager.Manager;
import net.alcuria.umbracraft.engine.screens.SetInputEnabled;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

/** Handles displaying any onscreen input options, from the touchpad to the menu
 * button and so on. This input is only available if a script is not halting it.
 * @author Andrew Keturi */
public class OnscreenInputManager extends Manager<OnscreenInput> implements EventListener, Disposable {

	private final InputHalter halter;
	private final Stage stage;
	private final TouchpadEntity touchpad;

	public OnscreenInputManager() {
		halter = new InputHalter();
		stage = new Stage(Game.view().getViewport());
		touchpad = new TouchpadEntity(stage);
		Gdx.input.setInputProcessor(new InputMultiplexer() {
			{
				addProcessor(touchpad);
				addProcessor(stage);
			}
		});
		add(touchpad);
		add(new MainMenuButton(stage));
		add(new InspectButton(stage));
		add(new DebugText(stage));
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose() {
		touchpad.dispose();
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		halter.check(event);
		if (event instanceof SetInputEnabled) {
			// ensure they aren't touching down??
			stage.touchUp(1, Config.viewHeight - 1, 1, 0);
		}
	}

	@Override
	public void render() {
		super.render();
		if (!halter.isHalted()) {
			stage.draw();
		}
	}

	@Override
	public void update() {
		if (!halter.isHalted()) {
			super.update();
			stage.act(Gdx.graphics.getDeltaTime());
		}
		// check if we've dragged offscreen and are letting go
		if (touchpad.isVisible() && !Gdx.input.isTouched()) {
			stage.touchUp(1, Config.viewHeight - 1, 0, 0);
			touchpad.touchUp(1, Config.viewHeight - 1, 0, 0);
		}
	}
}
