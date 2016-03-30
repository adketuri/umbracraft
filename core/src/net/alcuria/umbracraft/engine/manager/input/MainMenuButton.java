package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Drawables;
import net.alcuria.umbracraft.engine.screens.SetInputEnabled;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Creates a button that, when clicked, will take the player to the main
 * menu/pause menu.
 * @author Andrew Keturi */
public class MainMenuButton extends OnscreenInput implements InputProcessor {

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

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE) {
			onMenuPressed();
			return true;
		}
		return false;

	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	private void onMenuPressed() {
		Game.publisher().publish(new SetInputEnabled(false));
		Game.hud().openMainMenu();
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public void update() {
	}
}
