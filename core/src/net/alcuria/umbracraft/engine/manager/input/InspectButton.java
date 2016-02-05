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

/** Our inspect button. For now actions are dictated by finding a
 * {@link ControlledInputComponent} attached to the PLAYER entity.
 * @author Andrew Keturi */
public class InspectButton extends OnscreenInput {

	public InspectButton(Stage stage) {
		Button button = new Button(Drawables.texture("ui/inspectButtonUp"), Drawables.texture("ui/inspectButtonDown"));
		stage.addActor(button);
		button.setPosition(Config.viewWidth - 68, 20);
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
