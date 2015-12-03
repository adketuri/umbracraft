package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.engine.manager.Manager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;

public class OnscreenInputManager extends Manager<OnscreenInput> {

	public OnscreenInputManager() {
		if (Gdx.app.getType() == ApplicationType.iOS || Gdx.app.getType() == ApplicationType.Android) {
			add(new TouchpadEntity());
		}
	}
}
