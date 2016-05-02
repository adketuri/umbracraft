package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.anim.AnimationDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.listeners.Listener;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Given an {@link Entity} name and the name of an {@link AnimationDefinition},
 * this script removes an existing animation component and adds a new animation
 * component. If there is an {@link AnimationCollectionComponent} attached to
 * the targeting component, it is set to hidden.
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
	public ScriptCommand copy() {
		ShowAnimationScriptCommand cmd = new ShowAnimationScriptCommand();
		cmd.anim = anim;
		cmd.removeAfter = removeAfter;
		cmd.self = self;
		cmd.target = target;
		cmd.wait = wait;
		return cmd;
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
			if (StringUtils.isNotEmpty(anim)) {
				final AnimationComponent component = new AnimationComponent(Game.db().anim(anim));
				targetEntity.addComponent(component);
				// hide the collection from appearing
				AnimationCollectionComponent collection = targetEntity.getComponent(AnimationCollectionComponent.class);
				if (collection != null) {
					collection.setVisible(false);
				}
				if (wait) {
					component.setCompleteListener(new Listener() {

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
			} else {
				complete();
			}
		}

	}

	@Override
	public void update() {

	}
}
