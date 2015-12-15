package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.events.CameraTargetEvent;

/** A {@link ScriptCommand} to target the camera to a particular entity.
 * @author Andrew Keturi */
public class CameraTargetScriptCommand extends ScriptCommand {

	public String name;

	public CameraTargetScriptCommand(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return "Camera Target: " + name;
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

}
