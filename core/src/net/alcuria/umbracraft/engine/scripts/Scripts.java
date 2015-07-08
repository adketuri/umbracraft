package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;

public class Scripts {

	public static ScriptCommand changeAnim(final Entity entity, String anim) {
		return new ScriptCommand() {

			@Override
			public void update() {
				entity.removeAnimationComponent();
				entity.addComponent(new AnimationComponent(Game.db().anim("ChestAnim")));
				Game.log("Updated Components");
				complete();
			}

		};
	}

	public static ScriptCommand pause(final int time) {
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

	public static ScriptCommand showAnim(String name, String anim) {
		return new ScriptCommand() {

			@Override
			public void update() {
			}
		};
	}

	public static ScriptCommand test(final String message) {
		return new ScriptCommand() {

			@Override
			public void update() {
				Game.log("Update: " + message);
				complete();
			}
		};
	}

}
