package net.alcuria.umbracraft.save;

import net.alcuria.umbracraft.Game;
import net.alcuria.umbracraft.listeners.Listener.SuccessListener;
import net.alcuria.umbracraft.save.model.SaveProfile;
import net.alcuria.umbracraft.save.model.Saveable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

/** Manages save data from disk
 * @author Andrew Keturi */
public class DiskSaveManager implements Saveable {

	private static final String FILENAME = "save.json";
	private Array<SaveProfile> profiles = new Array<SaveProfile>();

	@Override
	public void dispose() {

	}

	@Override
	public Array<SaveProfile> getProfiles() {
		if (Game.db().config().savePath == null) {
			throw new NullPointerException("Save path not configured");
		}
		final FileHandle path = Gdx.files.external(Game.db().config().savePath + FILENAME);
		if (!path.exists()) {
			throw new NullPointerException("File not found: " + path);
		}
		Json json = new Json();
		json.setIgnoreUnknownFields(true);
		profiles = json.fromJson(Array.class, path);
		return profiles;
	}

	@Override
	public void saveProfiles(SuccessListener result) {
		if (result == null) {
			throw new NullPointerException("Result cannot be null");
		}
		if (Game.db().config().savePath == null) {
			throw new NullPointerException("Save path not configured");
		}
		Json json = new Json();
		json.setOutputType(OutputType.json);
		String jsonStr = json.prettyPrint(profiles);
		Gdx.files.external(Game.db().config().savePath + FILENAME).writeString(jsonStr, false);
		result.success("Game saved");
	}

	@Override
	public void setProfile(SaveProfile profile, int index) {
		profiles.set(index, profile);
	}

}
