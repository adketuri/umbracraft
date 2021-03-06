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

	private DirectedInputComponent component;
	@Tooltip("The entity id we wish to move")
	public String id = "";
	@Tooltip("If checked, movement happens instantaneously")
	public boolean instant;
	@Tooltip("If checked, movement is done relative to the entity's coordinates")
	public boolean relative;
	@Tooltip("If checked, ignore id and just move the entity this script is attached to")
	public boolean self;
	@Tooltip("Wait until the move is complete before continuing script execution")
	public boolean wait;
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
		cmd.instant = instant;
		return cmd;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return String.format("Move: %s to (%s, %s) %s, %s, %s", self ? "<self>" : id, x, y, relative ? "relative" : "absolute", instant ? "instant" : "", wait ? "Wait" : "");
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
			component = target.getComponent(DirectedInputComponent.class);
			if (component != null || instant) {
				// x/y can either be numbers or variables in the game's db
				int convertedX = StringUtils.isNumber(x) ? Integer.valueOf(x) : Game.variables().get(x);
				int convertedY = StringUtils.isNumber(y) ? Integer.valueOf(y) : Game.variables().get(y);
				if (instant) {
					target.velocity.x = 0;
					target.velocity.y = 0;
					if (relative) {
						target.position.x += (convertedX * Config.tileWidth) + Config.tileWidth / 2;
						target.position.y += (convertedY * Config.tileWidth) + Config.tileWidth / 2;
					} else {
						Game.log("target set to instant " + convertedX + " " + convertedY);
						target.position.x = convertedX * Config.tileWidth;
						target.position.y = convertedY * Config.tileWidth;
					}
					component.resetTarget((int) (target.position.x / Config.tileWidth), (int) (target.position.y / Config.tileWidth));
					complete();
				} else {
					if (relative) {
						component.setTarget((int) target.position.x / Config.tileWidth + convertedX, (int) target.position.y / Config.tileWidth + convertedY);
					} else {
						Game.log("target set to moved " + convertedX + " " + convertedY);
						component.setTarget(convertedX, convertedY);
					}
					if (!wait) {
						complete();
					}
				}
			} else {
				Game.error("Entity has no DirectedInputComponent so it cannot be moved.");
			}
		}
	}

	@Override
	public void update() {
		if (component != null) {
			if (component.hasNoMoves()) {
				complete();
			}
		}
	}
}
