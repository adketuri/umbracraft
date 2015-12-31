package net.alcuria.umbracraft.engine.scripts;

import net.alcuria.umbracraft.Config;
import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.definitions.ListDefinition;
import net.alcuria.umbracraft.definitions.entity.EntityDefinition;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.components.DirectedInputComponent;
import net.alcuria.umbracraft.engine.entities.Entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** Moves an entity to a particular pair of <b>tile</b> coordinates. Note, the
 * entity must contain a {@link DirectedInputComponent}.
 * @author Andrew Andrew Keturi */
public class MoveScriptCommand extends ScriptCommand {

	public String id = "";
	public boolean relative;
	public int x, y;

	public MoveScriptCommand() {
	}

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
		return String.format("Move: %s to (%d, %d, %s)", id, x, y, relative ? "relative" : "absolute");
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", new Array<String>() {
					{
						final ListDefinition<EntityDefinition> entities = Editor.db().entities();
						for (String key : entities.keys()) {
							add(entities.get(key).getName());
						}
					}
				});
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		Entity target = Game.entities().find(id);
		if (target != null) {
			DirectedInputComponent component = target.getComponent(DirectedInputComponent.class);
			if (component != null) {
				if (relative) {
					component.setTarget((int) target.position.x + x * Config.tileWidth, (int) target.position.y + y * Config.tileWidth);
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
