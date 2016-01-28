package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.events.Event;
import net.alcuria.umbracraft.engine.events.EventListener;
import net.alcuria.umbracraft.engine.manager.Manager;

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

	public OnscreenInputManager() {
		halter = new InputHalter();
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
		add(new InspectButton(stage));
		add(new DebugText(stage));
		Game.publisher().subscribe(this);
	}

	@Override
	public void dispose() {
		Game.publisher().unsubscribe(this);
	}

	@Override
	public void onEvent(Event event) {
		halter.check(event);
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
	}
}
