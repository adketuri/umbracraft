package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TouchpadEntity extends OnscreenInput {

	private final Stage stage;
	private final Touchpad touchpad;

	public TouchpadEntity() {
		TouchpadStyle style = new TouchpadStyle();
		style.background = new TextureRegionDrawable(Drawables.skin("ui/bg"));
		style.knob = new TextureRegionDrawable(Drawables.skin("ui/namePlate"));
		touchpad = new Touchpad(3, style);
		touchpad.setBounds(40, 40, 40, 40);
		stage = new Stage(Game.view().getViewport());
		stage.addActor(touchpad);
		Gdx.input.setInputProcessor(stage);
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
