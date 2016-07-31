package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Order;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationComponent;
import net.alcuria.umbracraft.engine.components.AnimationGroupComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Handles changing the {@link AnimationCollectionComponent} for an
 * {@link Entity}. One special case -- if there is no specified animation, it
 * re-enables the currently-active collection, if present.
 * @author Andrew */
public class SetAnimationCollectionScriptCommand extends ScriptCommand {
	@Order(2)
	public String anim = "";
	@Order(3)
	public boolean self;
	@Order(1)
	public String target = "";

	@Override
	public ScriptCommand copy() {
		SetAnimationCollectionScriptCommand cmd = new SetAnimationCollectionScriptCommand();
		cmd.anim = anim;
		cmd.self = self;
		cmd.target = target;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Set AnimCollection: %s, %s", StringUtils.isNotEmpty(anim) ? anim : "(Reset Visibility)", self ? "(Self)" : target);
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("target", Editor.db().entities().keys());
				put("anim", Editor.db().animCollections().keys());
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
			if (StringUtils.isNotEmpty(anim)) {
				targetEntity.removeComponent(AnimationCollectionComponent.class);
				targetEntity.addComponent(new AnimationCollectionComponent(Game.db().animCollection(anim)));
			} else {
				// our special case reset: we set the collection to visible and remove any animation currently attached
				AnimationCollectionComponent component = targetEntity.getComponent(AnimationCollectionComponent.class);
				if (component != null) {
					component.setVisible(true);
				}
				targetEntity.removeComponent(AnimationComponent.class);
				targetEntity.removeComponent(AnimationGroupComponent.class);
			}

		}
		complete();
	}

	@Override
	public void update() {

	}

}
