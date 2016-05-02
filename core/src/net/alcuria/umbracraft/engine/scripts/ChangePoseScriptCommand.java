package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent;
import net.alcuria.umbracraft.engine.components.AnimationCollectionComponent.Pose;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.O;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class ChangePoseScriptCommand extends ScriptCommand {

	@Tooltip("The new AnimationGroup we want")
	public Pose pose = Pose.IDLE;
	@Tooltip("The target we want to change")
	public String target = "";

	@Override
	public ScriptCommand copy() {
		ChangePoseScriptCommand cmd = new ChangePoseScriptCommand();
		cmd.target = target;
		cmd.pose = pose;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Change Pose: " + target + ", " + pose;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("target", Editor.db().entities().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		final Entity targetEntity = O.notNull(Game.entities().find(target));
		AnimationCollectionComponent collection = targetEntity.getComponent(AnimationCollectionComponent.class);
		if (collection != null) {
			collection.setPose(pose);
		} else {
			Game.error("Entity needs an AnimationCollectionComponent");
		}
		complete();
	}

	@Override
	public void update() {

	}

}
