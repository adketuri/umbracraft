package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.annotations.Tooltip;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.engine.screens.SetInputEnabled;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/** A {@link ScriptCommand} to initiate a battle.
 * @author Andrew Keturi */
public class BattleScriptCommand extends ScriptCommand {

	@Tooltip("The background to use for the battle")
	public String background = "";
	@Tooltip("The id of the enemy party we want to fight")
	public String id = "";
	@Tooltip("A script to invoke mid-battle")
	public String script;

	/** For deserialization */
	public BattleScriptCommand() {

	}

	@Override
	public ScriptCommand copy() {
		BattleScriptCommand command = new BattleScriptCommand();
		command.id = id;
		command.background = background;
		command.script = script;
		return command;
	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Battle: " + id + ", " + background;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("id", Editor.db().enemyGroups().keys());
				put("script", Editor.db().scripts().keys());
				put("background", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().battleBgPath));
			}
		};
	}

	@Override
	public void onCompleted() {
		final Entity player = Game.entities().find(Entity.PLAYER);
		if (player != null) {
			player.velocity.x = 0;
			player.velocity.y = 0;
		}
		Game.publisher().publish(new SetInputEnabled(true));
	}

	@Override
	public void onStarted(Entity entity) {
		Game.publisher().publish(new SetInputEnabled(false));
		Game.battle().start(this);
	}

	@Override
	public void update() {
		complete();
	}
}
