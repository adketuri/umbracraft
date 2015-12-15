package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.Listener;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

/** Given an {@link Entity} name and the name of an {@link AnimationDefinition},
 * this script removes an existing animation component and adds a new animation
 * component.
 * @author Andrew Keturi */
public class ShowAnimationScriptCommand extends ScriptCommand {

	public String name, anim;
	public boolean wait, removeAfter;

	/** @param name the name of the {@link Entity}
	 * @param anim the name of the {@link AnimationDefinition}
	 * @param wait if true wait until anim completes to mark command as complete
	 * @param removeAfter if true, removes the component upon completion */
	public ShowAnimationScriptCommand(final String name, final String anim, final boolean wait, final boolean removeAfter) {
		this.name = name;
		this.anim = anim;
		this.wait = wait;
		this.removeAfter = removeAfter;
	}

	@Override
	public String getName() {
		return "Show Animation: " + name;
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
}
