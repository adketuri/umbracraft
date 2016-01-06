package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Creates a button that, when clicked, will take the player to the main
 * menu/pause menu.
 * @author Andrew Keturi */
public class MainMenuButton extends OnscreenInput {

	public MainMenuButton(Stage stage) {
		Button button = new Button(Drawables.texture("ui/menuButtonUp"), Drawables.texture("ui/menuButtonDown"));
		stage.addActor(button);
		button.setPosition(Config.viewWidth - 40, Config.viewHeight - 40);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				onMenuPressed();
			}

		});
	}

	private void onMenuPressed() {
		Game.hud().openMainMenu();
	}

	@Override
	public void update() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			onMenuPressed();
		}
	}
}
