package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

/** Moves an entity to a particular pair of <b>tile</b> coordinates. Note, the
 * entity must contain a {@link DirectedInputComponent}.
 * @author Andrew Andrew Keturi */
public class MoveScriptCommand extends ScriptCommand {

	public String id;
	public boolean relative;
	public int x, y;

	/** @param id the entity name
	 * @param x the x position
	 * @param y the y position
	 * @param relative whether or not to use relative movement */
	public MoveScriptCommand(final String id, final int x, final int y, final boolean relative) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.relative = relative;
	}

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

}
