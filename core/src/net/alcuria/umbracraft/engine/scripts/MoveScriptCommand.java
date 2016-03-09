package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Moves an entity to a particular pair of <b>tile</b> coordinates. Note, the
 * entity must contain a {@link DirectedInputComponent}.
 * @author Andrew Andrew Keturi */
public class MoveScriptCommand extends ScriptCommand {

	@Tooltip("The entity id we wish to move")
	public String id = "";
	@Tooltip("If checked, movement is done relative to the entity's coordinates")
	public boolean relative;
	@Tooltip("If checked, ignore id and just move the entity this script is attached to")
	public boolean self;
	@Tooltip("The x coordinate (either a variable or constant)")
	public String x;
	@Tooltip("The y coordinate (either a variable or constant)")
	public String y;

	public MoveScriptCommand() {
	}

	/** @param id the entity name
	 * @param x the x position
	 * @param y the y position
	 * @param relative whether or not to use relative movement */
	public MoveScriptCommand(final String id, final String x, final String y, final boolean relative) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.relative = relative;
	}

	@Override
	public ScriptCommand copy() {
		MoveScriptCommand cmd = new MoveScriptCommand();
		cmd.id = id;
		cmd.relative = relative;
		cmd.self = self;
		cmd.x = x;
		cmd.y = y;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Move: %s to (%s, %s, %s)", id, x, y, relative ? "relative" : "absolute");
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", Editor.db().entities().keys());
				put("x", Editor.db().variables().keys());
				put("y", Editor.db().variables().keys());
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		Entity target = self ? entity : Game.entities().find(id);
		if (target != null) {
			DirectedInputComponent component = target.getComponent(DirectedInputComponent.class);
			if (component != null) {
				// x/y can either be numbers or variables in the game's db
				int convertedX = StringUtils.isNumber(x) ? Integer.valueOf(x) : Game.variables().get(x);
				int convertedY = StringUtils.isNumber(y) ? Integer.valueOf(y) : Game.variables().get(y);
				if (relative) {
					component.setTarget((int) target.position.x / Config.tileWidth + convertedX, (int) target.position.y / Config.tileWidth + convertedY);
				} else {
					component.setTarget(convertedX, convertedY);
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
