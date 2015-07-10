package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.Gdx;

/** Contains a collection of commands that, when executed, perform a single task
 * such as playing a sound effect or changing an animation.
 * @author Andrew Keturi */
public class Commands {

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
	 * @param wait if true wait until anim completes to mark command as complete
	 * @param removeAfter if true, removes the component upon completion
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand showAnim(final String name, final String anim, final boolean wait, final boolean removeAfter) {
		return new ScriptCommand() {

			@Override
			public void start() {
				super.start();
				final Entity entity = Game.entities().find(name);
				if (entity != null) {
					entity.removeAnimationComponent();
					final AnimationComponent component = new AnimationComponent(Game.db().anim(anim));
					entity.addComponent(component);
					if (wait) {
						component.setListener(new Listener() {

							@Override
							public void invoked() {
								if (removeAfter) {
									entity.removeAnimationComponent();
								}
								complete();
							}
						});
					} else {
						complete();
					}
				}
			}

			@Override
			public void update() {

			}
		};
	}

}
