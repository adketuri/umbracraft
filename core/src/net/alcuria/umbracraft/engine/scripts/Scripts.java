package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;

import com.badlogic.gdx.Gdx;

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

	public static ScriptCommand waitEvent(final int time) {
		return new ScriptCommand() {

			float curTime;

			@Override
			public void update() {
				curTime += Gdx.graphics.getDeltaTime();
				if (curTime > time) {
					complete();
				}

			}

		};
	}

}
