package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;
import net.alcuria.umbracraft.engine.events.MapChangedEvent;
import net.alcuria.umbracraft.engine.events.WindowHideEvent;
import net.alcuria.umbracraft.engine.events.WindowShowEvent;
import net.alcuria.umbracraft.engine.windows.message.MessageWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

/** Contains a collection of commands that, when executed, perform a single task
 * such as playing a sound effect or changing an animation.
 * @author Andrew Keturi */
public class Commands {

	public static ScriptCommand battle() {
		return new ScriptCommand() {

			@Override
			public String getName() {
				return "battle";
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onStarted() {
				Game.battle().start();
			}

			@Override
			public void update() {
				complete();
			}
		};
	}

	public static ScriptCommand cameraTarget(final String name) {
		return new ScriptCommand() {

			@Override
			public String getName() {
				return "camera";
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onStarted() {
				Entity entity = Game.entities().find(name);
				if (entity != null) {
					Game.publisher().publish(new CameraTargetEvent(entity));
				} else {
					Game.log("Entity not found: " + name + ". Cannot target.");
				}
				complete();
			}

			@Override
			public void update() {

			}
		};
	}

	public static ScriptCommand empty() {
		return new ScriptCommand() {

			@Override
			public String getName() {
				return "empty";
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onStarted() {

			}

			@Override
			public void update() {

			}
		};
	}

	/** A script to log a message to stdout
	 * @param message the message to display
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand log(final String message) {
		return new ScriptCommand() {

			@Override
			public String getName() {
				return "log";
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onStarted() {

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
			public String getName() {
				return "message: '" + message + "'";
			}

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

	/** Moves an entity to a particular pair of <b>tile</b> coordinates. Note,
	 * the entity must contain a {@link DirectedInputComponent}.
	 * @param id the entity name
	 * @param x the x position
	 * @param y the y position
	 * @param relative whether or not to use relative positioning
	 * @return the {@link ScriptCommand} */
	public static ScriptCommand move(final String id, final int x, final int y, final boolean relative) {
		return new ScriptCommand() {

			@Override
			public String getName() {
				return "move";
			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onStarted() {
				Entity entity = Game.entities().find(id);
				if (entity != null) {
					DirectedInputComponent component = entity.getComponent(DirectedInputComponent.class);
					if (component != null) {
						if (relative) {
							component.setTarget((int) entity.position.x + x * Config.tileWidth, (int) entity.position.y + y * Config.tileWidth);
						} else {
							component.setTarget(x * Config.tileWidth, y * Config.tileWidth);
						}
					} else {
						Game.error("Entity has no DirectedInputComponent so it cannot be moved.");
					}
				}
				complete();
			}

			@Override
			public void update() {

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
			public String getName() {
				return "pause";
			}

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
			public String getName() {
				return "showAnim";
			}

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

	public static ScriptCommand teleport(final String id, final int x, final int y) {
		return new ScriptCommand() {

			private final float FADE_TIME = 0.5f;
			private boolean teleported;
			private float time;

			@Override
			public String getName() {
				return "teleport";
			}

			@Override
			public void onCompleted() {
				teleported = false;
				time = 0;
			}

			@Override
			public void onStarted() {
				time = 0;
				teleported = false;
			}

			@Override
			public void update() {
				if (time < FADE_TIME) {
					// fade out
					final float color = (1 - time / FADE_TIME) * (1 - time / FADE_TIME);
					Game.batch().setColor(new Color(color, color, color, 1));
					time += Gdx.graphics.getDeltaTime();
				} else {
					// fade in
					final float color = ((time - FADE_TIME) / FADE_TIME) * ((time - FADE_TIME) / FADE_TIME);
					Game.batch().setColor(new Color(color, color, color, 1));
					time += Gdx.graphics.getDeltaTime();
					if (!teleported) {
						teleported = true;
						time = FADE_TIME;
						Game.publisher().publish(new MapChangedEvent(id));
					}
					if (time >= 2 * FADE_TIME) {
						complete();
					}
				}
			}
		};
	}

	private Commands() {
	};

}
