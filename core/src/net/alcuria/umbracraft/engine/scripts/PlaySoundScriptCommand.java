package net.alcuria.umbracraft.engine.scripts;

import java.util.Set;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.editor.Editor;
import net.alcuria.umbracraft.engine.entities.Entity;
import net.alcuria.umbracraft.util.FileUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class PlaySoundScriptCommand extends ScriptCommand {

	public String filename = "";

	@Override
	public ScriptCommand copy() {
		PlaySoundScriptCommand cmd = new PlaySoundScriptCommand();
		cmd.filename = filename;
		return cmd;

	}

	@Override
	public Set<String> getFilter() {
		return null;
	}

	@Override
	public String getName() {
		return "Sound: " + filename;
	}

	@Override
	public ObjectMap<String, Array<String>> getSuggestions() {
		return new ObjectMap<String, Array<String>>() {
			{
				put("filename", FileUtils.getFilesAt(Editor.db().config().projectPath + Editor.db().config().soundPath));
			}
		};
	}

	@Override
	public void onCompleted() {

	}

	@Override
	public void onStarted(Entity entity) {
		if (filename != null) {
			Game.sound("sounds/" + filename + ".wav");
		}
		complete();
	}

	@Override
	public void update() {

	}

}
