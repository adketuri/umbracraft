package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;
import net.alcuria.umbracraft.engine.windows.message.MessageWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

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
			public void onCompleted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStarted() {
				// TODO Auto-generated method stub

			}

			@Override
			public void update() {
				Game.log("Update: " + message);
				complete();
			}
		};
	}

	public static ScriptCommand message(final String message) {
		return new ScriptCommand() {

			private boolean dismissable = false;
			private MessageWindow window;

			@Override
			public void onCompleted() {
				Game.publisher().publish(new WindowHideEvent(window));
			}

			@Override
			public void onStarted() {
				window = new MessageWindow(message);
				Game.publisher().publish(new WindowShowEvent(window));
			}

			@Override
			public void update() {
				if (!Gdx.input.isKeyPressed(Keys.ENTER)) {
					dismissable = true;
				}
				if (dismissable && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
					complete();
				}
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
			public void onCompleted() {

			}

			@Override
			public void onStarted() {
				curTime = 0;
			}

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
			public void onCompleted() {
			}

			@Override
			public void onStarted() {
				final Entity entity = Game.entities().find(name);
				if (entity != null) {
					entity.removeComponent(AnimationComponent.class);
					final AnimationComponent component = new AnimationComponent(Game.db().anim(anim));
					entity.addComponent(component);
					if (wait) {
						component.setListener(new Listener() {

							@Override
							public void invoke() {
								if (removeAfter) {
									entity.removeComponent(AnimationComponent.class);
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
