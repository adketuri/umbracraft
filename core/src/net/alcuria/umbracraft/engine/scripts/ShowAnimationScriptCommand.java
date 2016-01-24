package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.listeners.Listener;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Given an {@link Entity} name and the name of an {@link AnimationDefinition},
 * this script removes an existing animation component and adds a new animation
 * component.
 * @author Andrew Keturi */
public class ShowAnimationScriptCommand extends ScriptCommand {

	public String target = "", anim = "";
	public boolean wait, removeAfter, self;

	public ShowAnimationScriptCommand() {
	}

	/** @param target the name of the {@link Entity}
	 * @param anim the name of the {@link AnimationDefinition}
	 * @param wait if true wait until anim completes to mark command as complete
	 * @param removeAfter if true, removes the component upon completion
	 * @param self if true, "target" of the animation is always the entity the
	 *        script is attached to */
	public ShowAnimationScriptCommand(final String target, final String anim, final boolean wait, final boolean removeAfter, final boolean self) {
		this.target = target;
		this.anim = anim;
		this.wait = wait;
		this.self = self;
		this.removeAfter = removeAfter;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Show Animation: %s, %s, %s, %s", anim, self ? "Self" : target, wait ? "Wait" : "Don't Wait", removeAfter ? "Remove" : "Don't Remove");
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("target", Editor.db().entities().keys());
				put("anim", Editor.db().anims().keys().toArray());
			}
		};
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onStarted(Entity entity) {
		final Entity targetEntity = self ? entity : Game.entities().find(target);
		if (targetEntity != null) {
			targetEntity.removeComponent(AnimationComponent.class);
			final AnimationComponent component = new AnimationComponent(Game.db().anim(anim));
			targetEntity.addComponent(component);
			if (wait) {
				component.setListener(new Listener() {

					@Override
					public void invoke() {
						if (removeAfter) {
							targetEntity.removeComponent(AnimationComponent.class);
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
