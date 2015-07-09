package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;

/** Contains a collection of commands that, when executed, perform a single task
 * such as playing a sound effect or changing an animation.
 * @author Andrew Keturi */
public class Commands {

	/** Changes the animation of a particular entity
	 * @param entity the {@link Entity}
	 * @param anim the {@link AnimationDefinition} name
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand changeAnim(final Entity entity, final String anim) {
		if (entity == null || anim == null) {
			return log("[ChangeAnim] Parameter is null: entity=" + entity + " anim=" + anim);
		}
		return new ScriptCommand() {

			@Override
			public void update() {
				entity.removeAnimationComponent();
				entity.addComponent(new AnimationComponent(Game.db().anim(anim)));
				Game.log("Updated Components");
				complete();
			}

		};
	}

	/** A script to log a message to stdout
	 * @param message the message to display
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand log(final String message) {
		return new ScriptCommand() {

			@Override
			public void update() {
				Game.log("Update: " + message);
				complete();
			}
		};
	}

	/** Pauses script execution for some amount of seconds
	 * @param time the time to pause, in seconds
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand pause(final float time) {
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

	/** Given an {@link Entity} name and the name of an
	 * {@link AnimationDefinition}, this method removes an existing animation
	 * component and adds a new animation component.
	 * @param name the name of the {@link Entity}
	 * @param anim the name of the {@link AnimationDefinition}
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand showAnim(final String name, final String anim) {
		return new ScriptCommand() {

			@Override
			public void update() {
				final Entity entity = Game.entities().find(name);
				if (entity != null) {
					entity.removeAnimationComponent();
					entity.addComponent(new AnimationComponent(Game.db().anim(anim)));
					complete();
				}
			}
		};
	}

}
