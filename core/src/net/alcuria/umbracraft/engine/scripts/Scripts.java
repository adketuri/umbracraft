package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;

public class Scripts {

	public static ScriptCommand testEvent(final String message) {
		return new ScriptCommand() {

			@Override
			public void update() {
				Game.log("Update: " + message);
				complete();
			}
		};
	}

}
