package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.components.ControlledInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class InspectButton extends OnscreenInput {

	public InspectButton(Stage stage) {
		Button button = new Button(Drawables.texture("ui/menuButtonUp"), Drawables.texture("ui/menuButtonDown"));
		stage.addActor(button);
		button.setPosition(Config.viewWidth - 40, 40);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				onInteractPressed();
			}

		});

	}

	private void onInteractPressed() {
		// TODO: do we really need to do a search every frame?
		Entity player = Game.entities().find(Entity.PLAYER);
		if (player != null) {
			ControlledInputComponent component = player.getComponent(ControlledInputComponent.class);
			if (component != null) {
				component.inspect();
			}
		}
	}

}
