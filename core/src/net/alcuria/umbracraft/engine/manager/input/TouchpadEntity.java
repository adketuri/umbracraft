package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.events.TouchpadCreatedEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** An entity for handling a {@link Touchpad} to provide mobile input.
 * @author Andrew Keturi */
public class TouchpadEntity extends OnscreenInput {

	private final Stage stage;
	private final Touchpad touchpad;

	public TouchpadEntity() {
		TouchpadStyle style = new TouchpadStyle();
		style.background = new TextureRegionDrawable(Drawables.skin("ui/bg"));
		style.knob = new TextureRegionDrawable(Drawables.skin("ui/namePlate"));
		touchpad = new Touchpad(3, style);
		touchpad.setBounds(40, 20, 60, 60);
		stage = new Stage(Game.view().getViewport());
		stage.addActor(touchpad);
		Gdx.input.setInputProcessor(stage);
		Game.publisher().publish(new TouchpadCreatedEvent(touchpad));
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void update() {
		stage.act(Gdx.graphics.getDeltaTime());
	}

}
