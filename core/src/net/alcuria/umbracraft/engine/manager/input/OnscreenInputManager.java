package net.alcuria.umbracraft.engine.manager.input;

import net.alcuria.umbracraft.engine.manager.Manager;

public class OnscreenInputManager extends Manager<OnscreenInput> {

	public OnscreenInputManager() {
		add(new TouchpadEntity());
	}
}
